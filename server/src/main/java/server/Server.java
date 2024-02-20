package server;

import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
import Handlers.GameHandler;
import Handlers.SessionHandler;
import spark.*;
import Service.*;

import java.util.Collection;
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

    private Object joinGame(Request request, Response response) throws DataAccessException{
        var authToken = getHeader(request);
        var body = getBody(request);
        response.type("application/java");
//        authorize user (I need to create this)!!!!!!!!!!!!!
        SessionHandler.authoriseUser(authToken, response);
        GameHandler.joinGame(body, authToken, response);
        return response.body();
    }

    private Object listGames(Request request, Response response) {
       try{
           String authToken = getHeader(request);
            Collection<GameData> list = GameService.listGames(authToken);
           if (list.isEmpty()) {
               return ""; // Return empty string if the list is empty
           }

           return new Gson().toJson(list);
       }catch(Exception e){
           response.status(500);
       }
       return "";
    }

    private Object logoutUser(Request request, Response response) throws DataAccessException {
        try{
            String authToken = getHeader(request);
            SessionHandler.authoriseUser(authToken, response);
            UserService.logout(authToken);
            return "";
        }
        catch(Exception e){
            response.status(500);
            return "{ \"message\": \"Error: Invalid login\" }";
        }
    }

    private Object loginUser(Request request, Response response) {
        try{
            UserData userData = new Gson().fromJson(request.body(), UserData.class);
            String username = userData.getUsername();
            String password = userData.getPassword();
            String authToken = UserService.login(username, password);
            if(authToken == null){
                response.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            }
            response.status(200);
            return "{ \"username\":\"" + username+ "\", \"authToken\":\"" + authToken+ "\" }";
        }catch(Exception e){
            response.status(500);
        }
        return "";
    }

    private Object createGame(Request request, Response response) {
        try {
            var authToken = getHeader(request);
            GameData data = new Gson().fromJson(request.body(), GameData.class);
            var body = data.getGameName();
//            var bodyObj = getBody(request);
//            var body = request.body();
//            if(authToken == null || authToken.isEmpty()){
//                response.status(401);
//                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
//            }
            if(body == null){
                response.status(400);
//                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }

            int gameID = GameService.createGame(authToken, body);
//            if(gameID == 0){
//                response.status(401);
//                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
//            }
            response.status(200);
            return "{ \"gameID\": "+ gameID+"}";
//            return new Gson().toJson(gameID);
        }catch(Exception e){
            response.status(500);
        }
        return "";
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
    private void exceptionHandler(Exception ex, Request request, Response response) {
        response.status();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}