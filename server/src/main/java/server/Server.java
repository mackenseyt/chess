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
        Spark.post("/game", this::createGame);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }
    private static String getHeader(Request request) {
        var header = request.headers("authorization");
        if (header == null) {
            return null;
        }
        return header;
    }

    private static Map<String, Object> getBody(Request request) {
        var body = new Gson().fromJson(request.body(), Map.class);
        if (body == null) {
            return null;
        }
        return body;
    }
    private Object createGame(Request request, Response response) {
        try {
            var authToken = getHeader(request);
            var bodyObj = getBody(request);
            var body = request.body();
            if(authToken == null || authToken.isEmpty()){
                response.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            if(bodyObj == null || bodyObj.isEmpty()){
                response.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }

            int gameID = gameService.createGame(authToken, body);
            response.status(200);
            return "{ \"gameID\": "+ gameID+"}";
        }catch(Exception e){
            response.status(500);
            return new Gson().toJson(Map.of("message", "Error: game not created"));
        }
    }
    private void exceptionHandler(Exception ex, Request request, Response response) {
        response.status();
    }

// registers user given a username, password, and email
    private Object registerUser(Request request, Response response){
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
            return new Gson().toJson(Map.of("message", "Error: User not registered"));
        }
    }


    private Object clearDatabase(Request request, Response response) {
        try{
            applicationService.clearAllData();
            response.status(200);
            return "";
        }catch(DataAccessException e){
            response.status(500);
            return new Gson().toJson(Map.of("message", "Error: Database not cleared"));
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}