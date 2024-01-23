package umm3601.user;

import java.io.IOException;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import umm3601.Controller;

/**
 * Controller that manages requests for info about users.
 */
public class UserController implements Controller {

  private UserDatabase userDatabase;

  /**
   * Construct a controller for users.
   * <p>
   * This loads the "database" of user info from a JSON file and stores that
   * internally so that (subsets of) users can be returned in response to
   * requests.
   *
   * @param database the `Database` containing user data
   */
  public UserController(UserDatabase userDatabase) {
    this.userDatabase = userDatabase;
  }

  /***
   * Create a database using the json file, use it as data source for a new
   * UserController
   *
   * Constructing the controller might throw an IOException if there are problems
   * reading from the JSON "database" file. If that happens we'll print out an
   * error message exit the program.
   *
   * @throws IOException
   */
  public static UserController buildUserController(String userDataFile) throws IOException {
    UserController userController = null;

    UserDatabase userDatabase = new UserDatabase(userDataFile);
    userController = new UserController(userDatabase);

    return userController;
  }

  /**
   * Get the single user specified by the `id` parameter in the request.
   *
   * @param ctx a Javalin HTTP context
   */
  public void getUser(Context ctx) {
    String id = ctx.pathParam("id");
    User user = userDatabase.getUser(id);
    if (user != null) {
      ctx.json(user);
      ctx.status(HttpStatus.OK);
    } else {
      throw new NotFoundResponse("No user with id " + id + " was found.");
    }
  }

  /**
   * Get a JSON response with a list of all the users in the "database".
   *
   * @param ctx a Javalin HTTP context
   */
  public void getUsers(Context ctx) {
    User[] users = userDatabase.listUsers(ctx.queryParamMap());
    ctx.json(users);
  }

  /**
   * Setup routes for the `user` collection endpoints.
   *
   * These endpoints are:
   * - `GET /api/users?age=NUMBER&company=STRING&name=STRING`
   * - List users, filtered using query parameters
   * - `age`, `company`, and `name` are optional query parameters
   * - `GET /api/users/:id`
   * - Get the specified user
   *
   * GROUPS SHOULD CREATE THEIR OWN CONTROLLER FOR TODOS THAT
   * IMPLEMENTS THE `Controller` INTERFACE.
   * You'll then implement the `addRoutes` method for that controller,
   * which will set up the routes for that data. The `Server#setupRoutes`
   * method will then call `addRoutes` for each controller, which will
   * add the routes for that controller's data.
   *
   * @param server The Javalin server instance
   */
  @Override
  public void addRoutes(Javalin server) {
    // Get specific user
    server.get("/api/users/{id}", this::getUser);

    // List users, filtered using query parameters
    server.get("/api/users", this::getUsers);
  }
}
