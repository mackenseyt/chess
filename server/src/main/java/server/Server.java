package server;

import Handlers.UserHandler;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
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
        Spark.post("/user", this::registerUser);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.delete("/db", this::clearDatabase);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object joinGame(Request request, Response response) {
        return "";
    }

    private Object listGames(Request request, Response response) {
        return "";
    }

    private Object logoutUser(Request request, Response response) {
        return "";
    }

    private Object loginUser(Request request, Response response) {

        return "";
    }

    private Object createGame(Request request, Response response) {
        try {
            var authToken = getHeader(request);
            GameData data = new Gson().fromJson(request.body(), GameData.class);
            var body = data.getGameName();
//            var bodyObj = getBody(request);
//            var body = request.body();
            if(authToken == null || authToken.isEmpty()){
                response.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            if(body == null){
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

    private static String getHeader(Request request) {
        var header = request.headers("authorization");
        if (header == null) {
            return null;
        }
        return header;
    }

    private static Map<String, Object> getBody(Request request) {
        var body = new Gson().fromJson(request.body(), Map.class);
        return body;
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}