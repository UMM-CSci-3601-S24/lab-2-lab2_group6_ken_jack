package umm3601;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.javalin.http.HttpCode;
import kong.unirest.HeaderNames;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

/**
 * A set of simple functional tests that checks that
 * the simple "demo" paths that we provided as examples
 * in the server actually do something reasonable.
 *
 * The two redirect tests ("/users" and "/todos") don't
 * actually test much besides that an HTML document is
 * returned. Anything else seemed too fragile to me.
 */
public class ServerFunctionalSpec {

  @BeforeAll
  public static void startServer() {
    Server.main(new String[0]);
  }

  @AfterAll
  public static void stopServer() {
    Server.stopServer();
  }

  @Test
  public void helloWorks() {
    HttpResponse<String> response = Unirest.get("http://localhost:4567/hello").asString();
    assertEquals(HttpCode.OK.getStatus(), response.getStatus(), "The '/hello' path should return OK status");
    assertEquals("Hello World", response.getBody(), "The '/hello' path should greet us with 'Hello World'");
  }

  @Test
  public void staticFilesWork() {
    String[] staticFileEndpoints = {"", "index.html", "users.html", "users", "todos.html", "todos"};
    for (String endpoint : staticFileEndpoints) {
      HttpResponse<String> response = Unirest.get("http://localhost:4567/" + endpoint).asString();
      assertEquals(HttpCode.OK.getStatus(),
          response.getStatus(),
          "The '/" + endpoint + "' path should return OK status");
      assertEquals("text/html",
          response.getHeaders().getFirst(HeaderNames.CONTENT_TYPE),
          "MIME type should be 'text/html'");
      assertTrue(response.getBody().contains("<!DOCTYPE html>"));
    }
  }
}
