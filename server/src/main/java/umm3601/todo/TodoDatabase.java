package umm3601.todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.util.Arrays;  // COMMENTED OUT FOR FOR LIST ALL TODOS TASK - Ken
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

//import io.javalin.http.BadRequestResponse;  // COMMENTED OUT FOR FOR LIST ALL TODOS TASK - Ken

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

/*   COMMENTED OUT FOR FOR LIST ALL TODOS TASK - Ken
    Get the single todo specified by the given ID. Return `null` if there is no
    todo with that ID.

    @param id the ID of the desired todo
    @return the todo with the given ID, or null if there is no todo with that ID

  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  } */

  /**
   * Get an array of all the todos satisfying the queries in the params.
   *
   * @param queryParams map of key-value pairs for the query
   * @return an array of all the todos matching the given criteria
   */
  public Todo[] listTodos(Map<String, List<String>> queryParams) {
    Todo[] filteredTodos = allTodos;

 /*  COMMENTED OUT, NEED THE DIFFERENT PARAMETERS FOR TODOS - Ken
  // Filter age if defined
  if (queryParams.containsKey("age")) {
    String ageParam = queryParams.get("age").get(0);
    try {
      int targetAge = Integer.parseInt(ageParam);
      filteredTodos = filterTodosByAge(filteredTodos, targetAge);
    }
    catch (NumberFormatException e) {
      throw new BadRequestResponse("Specified age '" + ageParam + "' can't be parsed to an integer");
      }
    } */
 /*  COMMENTED OUT, NEED THE DIFFERENT PARAMETERS FOR TODOS - Ken
  //Filter company if defined
    if (queryParams.containsKey("company")) {
      String targetOwner = queryParams.get("company").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, targetOwner);
    } */
    // Process other query parameters here...

    return filteredTodos;
  }

  /* COMMENTED OUT, NEED THE DIFFERENT PARAMETERS FOR TODOS - Ken
    Get an array of all the todos having the target age.

    @param todos     the list of todos to filter by age
    @param targetAge the target age to look for
    @return an array of all the todos from the given list that have the target
            age

  public Todo[] filterTodosByAge(Todo[] todos, int targetAge) {
    return Arrays.stream(todos).filter(x -> x.owner == targetOwner).toArray(Todo[]::new);
  } */

 /*  COMMENTED OUT, NEED THE DIFFERENT PARAMETERS FOR TODOS - Ken
    Get an array of all the todos having the target company.

    @param todos         the list of todos to filter by company
    @param targetCompany the target company to look for
    @return an array of all the todos from the given list that have the target
            company

  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  } */

}
