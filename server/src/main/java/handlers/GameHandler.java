package handlers;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;
import service.GameService;
import service.UserService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;

import spark.Response;

import java.util.Map;

public class GameHandler {
    private static final GameService gameService = new GameService();
    public static void joinGame(Map<String, Object> body, String authToken, Response response) throws DataAccessException {
        body.put("authToken", authToken);
        JoinGameRequest joinGameRequest = new Gson().fromJson(new Gson().toJson(body), JoinGameRequest.class);

        if (joinGameRequest.gameID() == 0) {
            response.status(400);
            throw new DataAccessException("bad request");
        }
        try {
            gameService.joinGame(joinGameRequest);
        } catch (DataAccessException e) {
            response.status(403);
            throw e;
        }

        response.body(new Gson().toJson(Map.of("message", "Success")));
    }

    public void listGames(Response response)throws DataAccessException{
        ListGameResponse listGameResponse = gameService.listGames();
        response.body(new Gson().toJson(listGameResponse));
    }

    public void createGame(Map<String, Object> bodyObj, Response response) throws DataAccessException {
        CreateGameRequest createGameRequest = new Gson().fromJson(new Gson().toJson(bodyObj), CreateGameRequest.class);
        if (createGameRequest == null){
            response.status(400);
            throw new DataAccessException("bad request");
        }
        CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);
        response.body(new Gson().toJson(createGameResponse));
    }
}
