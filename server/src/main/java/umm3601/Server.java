package umm3601;

import java.util.Arrays;

import io.javalin.Javalin;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.RouteOverviewPlugin;

public class Server {

  private static final int SERVER_PORT = 4567;
  public static final String CLIENT_DIRECTORY = "../client";

  // The `controllers` field is an array of all the `Controller` implementations
  // for the server. This is used to add routes to the server.
  private Controller[] controllers;

  /**
   * Construct a `Server` object that we'll use (via `startServer()`) to configure
   * and start the server.
   *
   * @param controllers The implementations of `Controller` used for this server
   */
  public Server(Controller[] controllers) {
    // This is what is known as a "defensive copy". We make a copy of
    // the array so that if the caller modifies the array after passing
    // it in, we don't have to worry about it. If we didn't do this,
    // the caller could modify the array after passing it in, and then
    // we'd be using the modified array without realizing it.
    this.controllers = Arrays.copyOf(controllers, controllers.length);
  }

  /**
   * Configure and start the server.
   *
   * This configures and starts the Javalin server, which will start listening for HTTP requests.
   * It also sets up the server to shut down gracefully if it's killed or if the
   * JVM is shut down.
   */
  void startServer() {
    Javalin javalin = configureJavalin();
    setupRoutes(javalin);
    javalin.start(SERVER_PORT);
  }

  /**
   * Configure the Javalin server. This includes
   *
   * - Adding a route overview plugin to make it easier to see what routes
   *   are available.
   * - Setting it up to shut down gracefully if it's killed or if the
   *   JVM is shut down.
   * - Setting up a handler for uncaught exceptions to return an HTTP 500
   *   error.
   *
   * @return The Javalin server instance
   */
  private Javalin configureJavalin() {
    /*
     * Create a Javalin server instance. We're using the "create" method
     * rather than the "start" method here because we want to set up some
     * things before the server actually starts. If we used "start" it would
     * start the server immediately and we wouldn't be able to do things like
     * set up routes. We'll call the "start" method later to actually start
     * the server.
     *
     * `plugins.register(new RouteOverviewPlugin("/api"))` adds
     * a helpful endpoint for us to use during development. In particular
     * `http://localhost:4567/api` shows all of the available endpoints and
     * what HTTP methods they use. (Replace `localhost` and `4567` with whatever server
     * and  port you're actually using, if they are different.)
     */
    Javalin server = Javalin.create(config -> {
      // This tells the server where to look for static files,
      // like HTML and JavaScript.
      config.staticFiles.add(CLIENT_DIRECTORY, Location.EXTERNAL);
      // This adds a Javalin plugin that will list all of the
      // routes/endpoints that we add below on a page reachable
      // via the "/api" path.
      config.plugins.register(new RouteOverviewPlugin("/api"));
    });

    // This catches any uncaught exceptions thrown in the server
    // code and turns them into a 500 response ("Internal Server
    // Error Response"). In general you'll like to *never* actually
    // return this, as it's an instance of the server crashing in
    // some way, and returning a 500 to your user is *super*
    // unhelpful to them. In a production system you'd almost
    // certainly want to use a logging library to log all errors
    // caught here so you'd know about them and could try to address
    // them.
    server.exception(Exception.class, (e, ctx) -> {
      throw new InternalServerErrorResponse(e.toString());
    });

    return server;
  }

  /**
   * Setup routes for the server.
   *
   * @param server The Javalin server instance
   */
  private void setupRoutes(Javalin server) {
    setDefaultRoutes(server);
    // Add the routes for each of the implementations of `Controller` in the
    // `controllers` array.
    for (Controller controller : controllers) {
      controller.addRoutes(server);
    }
  }

  /**
   * Set up the default routes for the server.
   *
   * This includes a simple "hello world" route (just for demonstration
   * purposes) and routes to redirect "simple" URLs to the actual
   * HTML pages.
   *
   * @param server The Javalin server instance
   */
  private void setDefaultRoutes(Javalin server) {
    // Simple example route
    server.get("/hello", ctx -> ctx.result("Hello World"));

    // Redirects to create simpler URLs
    server.get("/users", ctx -> ctx.redirect("/users.html"));
    server.get("/todos", ctx -> ctx.redirect("/todos.html"));
  }
}
