package server;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import spark.*;
import Service.*;
import java.nio.file.Paths;
import com.google.gson.Gson;

import javax.xml.crypto.Data;

public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::registerUser);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(Exception ex, Request request, Response response) {
        response.status();
    }

    private Object registerUser(Request request, Response response) throws DataAccessException {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String email = request.queryParams("email");

        // Perform user registration
        String authToken = userService.register(username, password, email);

        if (authToken != null) {
            response.status(200); // OK
            return authToken;
        } else {
            response.status(400); // Bad Request
            return "Registration failed";
        }
    }

    private Object clearDatabase(Request request, Response response) {

        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}