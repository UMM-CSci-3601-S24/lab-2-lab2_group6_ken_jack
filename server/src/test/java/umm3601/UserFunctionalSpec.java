package umm3601;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.javalin.http.HttpCode;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import umm3601.user.User;
import umm3601.user.UserDatabase;

/**
 * A set of simple functional tests that ensure that the
 * key *user* functionality of our server works.
 *
 * This isn't complete – we don't yet check that
 * appropriate errors are returns when we try, e.g.,
 * to get a user with an unknown or invalid ID.
 */
public class UserFunctionalSpec {
  private static UserDatabase userDatabase;

  @BeforeAll
  public static void startServer() {
    Server.main(null);
  }

  @AfterAll
  public static void stopServer() {
    Server.stopServer();
  }

  @BeforeAll
  public static void createDbAndMapper() throws IOException {
    userDatabase = new UserDatabase(Server.USER_DATA_FILE);
  }

  @Test
  public void canGetAllUsers() {
    User[] allUsers = userDatabase.listUsers(new HashMap<String, List<String>>());
    HttpResponse<User[]> response = Unirest.get("http://localhost:4567/api/users").asObject(User[].class);

    assertEquals(HttpCode.OK.getStatus(), response.getStatus(), "Getting all the users should return OK status");
    assertArrayEquals(allUsers, response.getBody(), "The list of users didn't match");
  }

  @Test
  public void canGetSingleUserById() {
    String id = "588935f5c668650dc77df581";
    HttpResponse<User> response = Unirest.get("http://localhost:4567/api/users/" + id).asObject(User.class);

    assertEquals(HttpCode.OK.getStatus(), response.getStatus(), "Getting an existing user should return OK status");
    assertEquals(userDatabase.getUser(id), response.getBody(), "The user didn't match");
  }
}
