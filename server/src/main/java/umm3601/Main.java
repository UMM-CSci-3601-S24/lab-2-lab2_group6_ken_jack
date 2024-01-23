package umm3601;

import java.io.IOException;

import umm3601.user.UserController;

public class Main {
  public static final String USER_DATA_FILE = "/users.json";

  public static void main(String[] args) throws IOException {

    // The implementations of `Controller` used for the server. These will presumably
    // be one or more controllers, each of which implements the `Controller` interface.
    // You'll add your own controllers in `getControllers` as you create them.
    final Controller[] controllers = Main.getControllers();

    // Construct the server
    Server server = new Server(controllers);

    // Start the server
    server.startServer();
  }

  /**
   * Get the implementations of `Controller` used for the server.
   *
   * These will presumably be one or more controllers, each of which
   * implements the `Controller` interface. You'll add your own controllers
   * in to the array returned by this method as you create them.
   *
   * @return An array of implementations of `Controller` for the server.
   * @throws IOException
   */
  static Controller[] getControllers() throws IOException {
    Controller[] controllers = new Controller[] {
      // You would add additional controllers here, as you create them,
      // although you need to make sure that each of your new controllers implements
      // the `Controller` interface.
      UserController.buildUserController(USER_DATA_FILE)
    };
    return controllers;
  }

}
