package server;

import dataAccess.DataAccessException;

import dataAccess.sqlDao.AuthSqlDao;
import handlers.UserHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import handlers.GameHandler;
import handlers.SessionHandler;
import spark.*;
import service.*;

import java.util.Map;

import com.google.gson.Gson;

public class Server {

    private final GameHandler gameHandler = new GameHandler();
    private final SessionHandler sessionHandler = new SessionHandler();
    private final UserHandler userHandler = new UserHandler();


    public static void main(String[] args){
        try{
            int port = Integer.parseInt(args[0]);
            Server temp = new Server();
            temp.run(port);
        }
        catch (Exception exp){
            System.err.println(exp.getMessage());
        }
    }

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
        Spark.exception(DataAccessException.class, this::errorHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object joinGame(Request request, Response response) throws DataAccessException{
        var authToken = getHeader(request);
        var body = getBody(request);
        response.type("application/java");
        userHandler.authorizeUser(authToken, response);
        gameHandler.joinGame(body, authToken, response);
        return response.body();
    }

    private Object listGames(Request request, Response response) throws DataAccessException {
       var authToken = getHeader(request);
        response.type("application/json");
        userHandler.authorizeUser(authToken, response);
        gameHandler.listGames(response);
        return response.body();
    }

    private Object logoutUser(Request request, Response response) throws DataAccessException {
        var authToken = getHeader(request);
        response.type("application/json");
        userHandler.authorizeUser(authToken, response);
        sessionHandler.logout(authToken, response);
        return response.body();

    }

    private Object loginUser(Request request, Response response) throws DataAccessException {
        var body = getBody(request);
        response.type("application/json");
        sessionHandler.login(body, response);
        return response.body();
    }

    private Object createGame(Request request, Response response) throws DataAccessException{
        var authToken = getHeader(request);
        var bodyObj = getBody(request);
        response.type("application/json");
        userHandler.authorizeUser(authToken, response);
        gameHandler.createGame(bodyObj, response);
        return response.body();
    }


// registers user given a username, password, and email
    private Object registerUser(Request request, Response response)throws DataAccessException{
        var bodyObj = getBody(request);
        response.type("application/json");
        userHandler.registerUser(bodyObj, response);
        return response.body();
    }


    private Object clearDatabase(Request request, Response response) throws DataAccessException {
        sessionHandler.clearDatabase(response);
        return response.body();
    }

    private static String getHeader(Request request) {
        var header = request.headers("authorization");
        if (header == null) {
            throw new RuntimeException("missing required header");
        }
        return header;
    }

    private static Map<String, Object> getBody(Request request) {
        var body = new Gson().fromJson(request.body(), Map.class);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
    private Object errorHandler(DataAccessException e, Request request, Response response) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        response.type("application/json");
        if (response.status() == 200) {
            response.status(500);
        }
        response.body(body);
        return body;
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}