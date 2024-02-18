package server;

import Handlers.UserHandler;
import dataAccess.DataAccessException;
import model.UserData;
import spark.*;
import Service.*;

import java.util.Map;

import com.google.gson.Gson;

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

// registers user given a username, password, and email
    private Object registerUser(Request request, Response response) throws DataAccessException{
        try {
            UserData userData = new Gson().fromJson(request.body(), UserData.class);
            // Extract username, password, and email from the UserData object
            String username = userData.getUsername();
            String password = userData.getPassword();
            String email = userData.getEmail();

            String authToken = UserService.register(username, password, email);

//            handles if they give you bad data or a username that already exists
            if(username == null || password == null || email == null){
                response.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            // Perform user registration
            if(authToken == null){
                response.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
            response.status(200);
            return new Gson().toJson(Map.of("username", username, "authToken", authToken));

        }catch(DataAccessException e){
            response.status(500);
            return new Gson().toJson(Map.of("message", "Error: description"));
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