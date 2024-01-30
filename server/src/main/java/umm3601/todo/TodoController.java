package umm3601.todo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;

import umm3601.Controller;

/**
 * Controller that manages requests for info about todos.
 */
public class TodoController implements Controller {

  private TodoDatabase todoDatabase;

  /**
   * Construct a controller for todos.
   * <p>
   * This loads the "database" of todo info from a JSON file and stores that
   * internally so that (subsets of) todos can be returned in response to
   * requests.
   *
   * @param database the `Database` containing todo data
   */
  public TodoController(TodoDatabase todoDatabase) {
    this.todoDatabase = todoDatabase;
  }

  /**
   * Create a database using the json file, use it as data source for a new
   * TodoController
   *
   * Constructing the controller might throw an IOException if there are problems
   * reading from the JSON "database" file. If that happens we'll print out an
   * error message exit the program.
   *
   * @throws IOException
   */
  public static TodoController buildTodoController(String todoDataFile) throws IOException {
    TodoController todoController = null;

    TodoDatabase todoDatabase = new TodoDatabase(todoDataFile);
    todoController = new TodoController(todoDatabase);

    return todoController;
  }

  /**
   * Get the single todo specified by the `id` parameter in the request.
   *
   * @param ctx a Javalin HTTP context
   */
  public void getTodo(Context ctx) {
    String id = ctx.pathParam("id");
    Todo todo = todoDatabase.getTodo(id);
    if (todo != null) {
      ctx.json(todo);
      ctx.status(HttpStatus.OK);
    } else {
      throw new NotFoundResponse("No todo with id " + id + " was found.");
    }
  }

  /**
   * Handles HTTP requests to list all todos. It retrieves query parameters from
   * the request, uses them to filter and sort the todos in the database, and
   * sends the
   * resulting list of todos as a JSON response.
   *
   * @param ctx a Javalin HTTP context
   */
  public void getTodos(Context ctx) {
    Map<String, List<String>> queryParams = ctx.queryParamMap();
    Todo[] todos = todoDatabase.getTodos(queryParams);
    ctx.json(todos);
  }

  /**
   * * Adds route handlers to the Javalin server instance.
   *
   * Defines two GET routes:
   * 1. "/api/todos/{id}" - Retrieves a single todo item by its ID.
   * 2. "/api/todos" - Retrieves a list of todo items, with optional filtering
   * based on query parameters.
   *
   * @param server The Javalin server instance
   */
  @Override
  public void addRoutes(Javalin server) {
    // Gets the single todo with the given ID
    server.get("/api/todos/{id}", this::getTodo);

    // Gets todos with any given filters
    server.get("/api/todos", this::getTodos);
  }
}
