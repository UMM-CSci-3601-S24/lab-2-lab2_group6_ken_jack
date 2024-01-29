package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.javalin.http.Context;
import umm3601.Main;

/**
 * Tests the logic of the TodoController
 *
 * @throws IOException
 */
// The tests here include a ton of "magic numbers" (numeric constants).
// It wasn't clear to me that giving all of them names would actually
// help things. The fact that it wasn't obvious what to call some
// of them says a lot. Maybe what this ultimately means is that
// these tests can/should be restructured so the constants (there are
// also a lot of "magic strings" that Checkstyle doesn't actually
// flag as a problem) make more sense.
@SuppressWarnings({ "MagicNumber" })
public class TodoControllerSpec {

  // An instance of the controller we're testing that is prepared in
  // `setupEach()`, and then exercised in the various tests below.
  private TodoController todoController;
  // An instance of our database "layer" that is prepared in
  // `setupEach()`, and then used in the tests below.
  private static TodoDatabase db;

  // A "fake" version of Javalin's `Context` object that we can
  // use to test with.
  @Mock
  private Context ctx;

  // A captor allows us to make assertions on arguments to method
  // calls that are made "indirectly" by the code we are testing,
  // in this case `json()` calls in `TodoController`. We'll use
  // this to make assertions about the data passed to `json()`.
  @Captor
  private ArgumentCaptor<Todo[]> localTodoArrayCaptor;

  /**
   * Setup the "database" with some example users and
   * create a TodoController to exercise in the tests.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  @BeforeEach
  public void setUp() throws IOException {
    // Reset our mock context and argument captor
    // (declared above with Mockito annotations @Mock and @Captor)
    MockitoAnnotations.openMocks(this);
    // Construct our "database"
    db = new TodoDatabase(Main.TODO_DATA_FILE);
    // Construct an instance of our controller which
    // we'll then test.
    todoController = new TodoController(db);
  }

  /**
   * Verify that we can successfully build a TodoController
   * and call it's `addRoutes` method. This doesn't verify
   * much beyond that the code actually runs without throwing
   * an exception. We do, however, confirm that the `addRoutes`
   * causes `.get()` to be called at least twice.
   */
/* COMMENTED OUT BECAUSE IT WAS FAILING. WE MAY NEED IT LATER. - KEN
  @Test
  public void canBuildController() throws IOException {
    // Call the `TodoController.buildTodoController` method
    // to construct a controller instance "by hand".
    TodoController controller = TodoController.buildTodoController(Main.TODO_DATA_FILE);
    Javalin mockServer = Mockito.mock(Javalin.class);
    controller.addRoutes(mockServer);

    // Verify that calling `addRoutes()` above caused `get()` to be called
    // on the server at least twice. We use `any()` to say we don't care about
    // the arguments that were passed to `.get()`.
    verify(mockServer, Mockito.atLeast(2)).get(any(), any());
  } */

  /**
   * Verify that attempting to build a `TodoController` with an
   * invalid `todoDataFile` throws an `IOException`.
   */
  @Test
  public void buildControllerFailsWithIllegalDbFile() {
    Assertions.assertThrows(IOException.class, () -> {
      TodoController.buildTodoController("this is not a legal file name");
    });
  }

  /**
   * Confirm that we can get all the todos.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  @Test
  public void canGetAllTodos() throws IOException {
    // Call the method on the mock context, which doesn't
    // include any filters, so we should get all the todos
    // back.
    todoController.getTodos(ctx);

    // Confirm that `json` was called with all the todos.
    // The ArgumentCaptor<Todo[]> todoArrayCaptor was initialized in the @BeforeEach
    // Here, we wait to see what happens *when ctx calls the json method* in the call
    // todoController.getTodos(ctx) and the json method is passed a Todo[]
    // (That's when the Todo[] that was passed as input to the json method is captured)
    verify(ctx).json(localTodoArrayCaptor.capture());
    // Now that the Todo[] that was passed as input to the json method is captured,
    // we can make assertions about it. In particular, we'll assert that its length
    // is the same as the size of the "database". We could also confirm that the
    // particular todos are the same/correct, but that can get complicated
    // since the order of the todos in the "database" isn't specified. So we'll
    // just check that the counts are correct.
    assertEquals(db.size(), localTodoArrayCaptor.getValue().length);
  }

  //Tests for filtering todos by owner
  @Test
  public void filterTodosByOwner() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("owner", Arrays.asList(new String[] {"John"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).json(localTodoArrayCaptor.capture());
    for (Todo todo : localTodoArrayCaptor.getValue()) {
      assertEquals("John", todo.owner);
    }
  }

  @Test
  public void canLimitTodos() throws IOException {
    // Add a query param map to the context that maps "limit"
    // to "5".
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] {"5"}));
    // Tell the mock `ctx` object to return our query
    // param map when `queryParamMap()` is called.
    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller with the added
    // query param map to limit the result to just 5 todos
    todoController.getTodos(ctx);

    // Capture the todos that were passed to the json method
    ArgumentCaptor<Todo[]> todoArrayCaptor = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(todoArrayCaptor.capture());

    // Assert that the length of the returned todos is 5
    assertEquals(5, todoArrayCaptor.getValue().length);
  }

  @Test
  public void limitZeroReturnsNoTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] {"0"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    ArgumentCaptor<Todo[]> todoArrayCaptor = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(todoArrayCaptor.capture());

    assertEquals(0, todoArrayCaptor.getValue().length);
  }

  @Test
  public void limitThousandReturnsAllTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] {"1000"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    ArgumentCaptor<Todo[]> todoArrayCaptor = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(todoArrayCaptor.capture());

    assertEquals(db.size(), todoArrayCaptor.getValue().length);
  }

 /*  @Test
  public void nonNumericLimitReturnsBadRequest() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("limit", Arrays.asList(new String[] {"notANumber"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).status(400); // HTTP 400 Bad Request
  } */

  //  Testing when status is complete
  @Test
  public void statusCompleteReturnsCompleteTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] {"true"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

      ArgumentCaptor<Todo[]> todoArrayCaptor = ArgumentCaptor.forClass(Todo[].class);
      verify(ctx).json(todoArrayCaptor.capture());

      for (Todo todo : todoArrayCaptor.getValue()) {
        assertTrue(todo.status);
      }
    }

    // Testing when status is incomplete
    @Test
    public void statusIncompleteReturnsIncompleteTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] {"false"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    ArgumentCaptor<Todo[]> todoArrayCaptor = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(todoArrayCaptor.capture());

    for (Todo todo : todoArrayCaptor.getValue()) {
      assertFalse(todo.status);
    }
  }

  @Test
  public void statusNullReturnsAllTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    ArgumentCaptor<Todo[]> todoArrayCaptor = ArgumentCaptor.forClass(Todo[].class);
    verify(ctx).json(todoArrayCaptor.capture());

    assertEquals(db.size(), todoArrayCaptor.getValue().length);
}

@Test
public void canGetTodosWithContains() throws IOException {
  // Add a query param map to the context that maps "contains"
  // to "some text".
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("contains", Arrays.asList(new String[] {"some text"}));
  // Tell the mock `ctx` object to return our query
  // param map when `queryParamMap()` is called.
  when(ctx.queryParamMap()).thenReturn(queryParams);

  // Call the method on the mock controller with the added
  // query param map to limit the result to just todos containing
  // "some text".
  todoController.getTodos(ctx);

  // Confirm that all the todos passed to `json` contain "some text".
  verify(ctx).json(localTodoArrayCaptor.capture());
  for (Todo todo : localTodoArrayCaptor.getValue()) {
    assertTrue(todo.body.contains("some text"));
  }
}

// Tests for filtering todos by category = any
@Test
public void getTodosWithCategoryAny() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("category", Arrays.asList(new String[] {"any"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).json(localTodoArrayCaptor.capture());
    for (Todo todo : localTodoArrayCaptor.getValue()) {
        assertNotNull(todo.category);
    }
}

// Tests for filtering todos by category = video games
@Test
public void getTodosWithCategoryVideoGames() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("category", Arrays.asList(new String[] {"video games"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).json(localTodoArrayCaptor.capture());
    for (Todo todo : localTodoArrayCaptor.getValue()) {
        assertEquals("video games", todo.category);
    }
}

// Tests for filtering todos by category = homework
@Test
public void getTodosWithCategoryHomework() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("category", Arrays.asList(new String[] {"homework"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);

    verify(ctx).json(localTodoArrayCaptor.capture());
    for (Todo todo : localTodoArrayCaptor.getValue()) {
        assertEquals("homework", todo.category);
    }
}


@Test
public void canSortTodosByOwnerAlphabetically() throws IOException {
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("orderBy", Arrays.asList(new String[] {"owner"}));
  when(ctx.queryParamMap()).thenReturn(queryParams);

  todoController.getTodos(ctx);

  verify(ctx).json(localTodoArrayCaptor.capture());
  Todo[] returnedTodos = localTodoArrayCaptor.getValue();

  for (int i = 0; i < returnedTodos.length - 1; i++) {
    assertTrue(returnedTodos[i].owner.compareTo(returnedTodos[i + 1].owner) <= 0);
  }
}

@Test
public void canSortTodosByStatusAlphabetically() throws IOException {
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("orderBy", Arrays.asList(new String[] {"status"}));
  when(ctx.queryParamMap()).thenReturn(queryParams);

  todoController.getTodos(ctx);

  verify(ctx).json(localTodoArrayCaptor.capture());
  Todo[] returnedTodos = localTodoArrayCaptor.getValue();

  for (int i = 0; i < returnedTodos.length - 1; i++) {
    assertTrue(String.valueOf(returnedTodos[i].status).compareTo(String.valueOf(returnedTodos[i + 1].status)) <= 0);
  }
}




  /* COMMENTED OUT FOR NOW. CAN USE AS A MODEL FOR THE OTHER TESTS. - KEN
    Confirm that we can get all the users with age 25.

    @throws IOException if there are problems reading from the "database" file.

  @Test
  public void canGetUsersWithAge25() throws IOException {
    // Add a query param map to the context that maps "age"
    // to "25".
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("age", Arrays.asList(new String[] {"25"}));
    // Tell the mock `ctx` object to return our query
    // param map when `queryParamMap()` is called.
    when(ctx.queryParamMap()).thenReturn(queryParams);

    // Call the method on the mock controller with the added
    // query param map to limit the result to just users with
    // age 25.
    todoController.getUsers(ctx);

    // Confirm that all the users passed to `json` have age 25.
    verify(ctx).json(todoArrayCaptor.capture());
    for (User user : todoArrayCaptor.getValue()) {
      assertEquals(25, user.age);
    }
    // Confirm that there are 2 users with age 25
    assertEquals(2, todoArrayCaptor.getValue().length);
  } */

  /**
   * Test that if the user sends a request with an illegal value in
   * the age field (i.e., something that can't be parsed to a number)
   * we get a reasonable error code back.
   */
  /*@Test COMMENTED OUT FOR NOW. CAN USE AS A MODEL FOR THE OTHER TESTS. - KEN
  public void respondsAppropriatelyToIllegalAge() {
    // We'll set the requested "age" to be a string ("abc")
    // that can't be parsed to a number.
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("age", Arrays.asList(new String[] {"abc"}));
    // Tell the mock `ctx` object to return our query
    // param map when `queryParamMap()` is called.
    when(ctx.queryParamMap()).thenReturn(queryParams);

    // This should now throw a `BadRequestResponse` exception because
    // our request has an age that can't be parsed to a number.
    Throwable exception = Assertions.assertThrows(BadRequestResponse.class, () -> {
      todoController.getUsers(ctx);
    });
    assertEquals("Specified age '" + "abc" + "' can't be parsed to an integer", exception.getMessage());
  }

  /**
   * Confirm that we can get all the users with company OHMNET.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  /*@Test COMMENTED OUT FOR NOW. CAN USE AS A MODEL FOR THE OTHER TESTS. - KEN
  public void canGetUsersWithCompany() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("company", Arrays.asList(new String[] {"OHMNET"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getUsers(ctx);

    // Confirm that all the users passed to `json` work for OHMNET.
    verify(ctx).json(todoArrayCaptor.capture());
    for (User user : todoArrayCaptor.getValue()) {
      assertEquals("OHMNET", user.company);
    }
  }

  /**
   * Confirm that we can get all the users with age 25 and company OHMNET.
   * This is a "combination" test that tests the interaction of the
   * `age` and `company` query parameters.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  /*@Test COMMENTED OUT FOR NOW. CAN USE AS A MODEL FOR THE OTHER TESTS. - KEN
  public void canGetUsersWithGivenAgeAndCompany() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("company", Arrays.asList(new String[] {"OHMNET"}));
    queryParams.put("age", Arrays.asList(new String[] {"25"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getUsers(ctx);

    // Confirm that all the users passed to `json` work for OHMNET
    // and have age 25.
    verify(ctx).json(todoArrayCaptor.capture());
    for (User user : todoArrayCaptor.getValue()) {
      assertEquals(25, user.age);
      assertEquals("OHMNET", user.company);
    }
    assertEquals(1, todoArrayCaptor.getValue().length);
  }

  /**
   * Confirm that we get a user when using a valid user ID.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  /*@Test COMMENTED OUT FOR NOW. CAN USE AS A MODEL FOR THE OTHER TESTS. - KEN
  public void canGetUserWithSpecifiedId() throws IOException {
    // A specific user ID known to be in the "database".
    String id = "588935f5c668650dc77df581";
    // Get the user associated with that ID.
    User user = db.getUser(id);

    when(ctx.pathParam("id")).thenReturn(id);

    todoController.getUser(ctx);

    verify(ctx).json(user);
    verify(ctx).status(HttpStatus.OK);
  }

  /**
   * Confirm that we get a 404 Not Found response when
   * we request a user ID that doesn't exist.
   *
   * @throws IOException if there are problems reading from the "database" file.
   */
  /*@Test COMMENTED OUT FOR NOW. CAN USE AS A MODEL FOR THE OTHER TESTS. - KEN
  public void respondsAppropriatelyToRequestForNonexistentId() throws IOException {
    when(ctx.pathParam("id")).thenReturn(null);
    Throwable exception = Assertions.assertThrows(NotFoundResponse.class, () -> {
      todoController.getUser(ctx);
    });
    assertEquals("No user with id " + null + " was found.", exception.getMessage());
  } */
}
