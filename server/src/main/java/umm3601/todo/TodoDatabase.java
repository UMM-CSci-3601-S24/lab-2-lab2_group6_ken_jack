package umm3601.todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.BadRequestResponse;

/**
 * A fake "database" of todo info
 * <p>
 * Since we don't want to complicate this lab with a real database, we're going
 * to instead just read a bunch of todo data from a specified JSON file, and
 * then provide various database-like methods that allow the `TodoController` to
 * "query" the "database".
 */
public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {
    // The `.getResourceAsStream` method searches for the given resource in
    // the classpath, and returns `null` if it isn't found. We want to throw
    // an IOException if the data file isn't found, so we need to check for
    // `null` ourselves, and throw an IOException if necessary.
    InputStream resourceAsStream = getClass().getResourceAsStream(todoDataFile);
    if (resourceAsStream == null) {
      throw new IOException("Could not find " + todoDataFile);
    }
    InputStreamReader reader = new InputStreamReader(resourceAsStream);
    // A Jackson JSON mapper knows how to parse JSON into sensible 'Todo'
    // objects.
    ObjectMapper objectMapper = new ObjectMapper();
    // Read our todo data file into an array of todo objects.
    allTodos = objectMapper.readValue(reader, Todo[].class);
  }

  public int size() {
    return allTodos.length;
  }

  /**
   * Get the single todo specified by the given ID. Return `null` if there is no
   * todo with that ID.
   *
   * @param id the ID of the desired todo
   * @return the todo with the given ID, or null if there is no todo with that ID
   */
  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  /**
   * Get an array of all the todos satisfying the queries in the params.
   *
   * @param queryParams map of key-value pairs for the query
   * @return an array of all the todos matching the given criteria
   */
  public Todo[] getTodos(Map<String, List<String>> queryParams) {
    Todo[] filteredTodos = this.allTodos;

    // contains filter
    if (queryParams.containsKey("contains")) {
      String containsParam = queryParams.get("contains").get(0);
      filteredTodos = Arrays.stream(filteredTodos)
          .filter(t -> t.body.contains(containsParam))
          .toArray(Todo[]::new);
    }

    // Filter owner if defined
    if (queryParams.containsKey("owner")) {
      String targetOwner = queryParams.get("owner").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, targetOwner);
    }

    // Filter status if defined
    if (queryParams.containsKey("status")) { // if the query contains status
      String statusParam = queryParams.get("status").get(0); // get the status
      boolean targetStatus = "complete".equalsIgnoreCase(statusParam); // parse the status to a boolean
      filteredTodos = Arrays.stream(filteredTodos) // filter the todos by the status
          .filter(todo -> todo.status == targetStatus) // if the status is the same as the target status
          .toArray(Todo[]::new); // return the filtered todos
    }

    // Filter category if defined
    if (queryParams.containsKey("category")) {
      String targetCategory = queryParams.get("category").get(0);
      filteredTodos = filterTodosByCategory(filteredTodos, targetCategory);
    }

    // Sorts the todos by the given parameter (orderBy, body, status, category,
    // owner)
    if (queryParams.containsKey("orderBy")) {
      String orderBy = queryParams.get("orderBy").get(0);
      Comparator<Todo> comparator = null;
      // Switch statement to determine which comparator to use
      switch (orderBy) {
        case "body":
          comparator = Comparator.comparing(todo -> todo.body);
          break;
        case "status":
          comparator = Comparator.comparing(todo -> Boolean.toString(todo.status));
          break;
        case "category":
          comparator = Comparator.comparing(todo -> todo.category);
          break;
        case "owner":
          comparator = Comparator.comparing(todo -> todo.owner);
          break;
        default:
          // Do nothing
          break;
      }
      if (comparator != null) {
        Arrays.sort(filteredTodos, comparator);
      }
    }

    // Filter limit if defined
    if (queryParams.containsKey("limit")) {
      String limitParam = queryParams.get("limit").get(0);
      try {
        int limit = Integer.parseInt(limitParam);
        filteredTodos = Arrays.stream(filteredTodos)
            .limit(limit)
            .toArray(Todo[]::new);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified limit '" + limitParam + "' can't be parsed to an integer");
      }
    }
    return filteredTodos;
  }

  /**
   * Get an array of all the todos having the target owner.
   *
   * @param todos       the list of todos to filter by owner
   * @param targetOwner the target owner to look for
   * @return an array of all the todos from the given list that have the target
   *         Owner
   */
  // Filter owner method
  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  }

  // Filter category method
  public Todo[] filterTodosByCategory(Todo[] todos, String targetCategory) {
    return Arrays.stream(todos)
        .filter(todo -> todo.category.equals(targetCategory))
        .toArray(Todo[]::new);
  }

}
