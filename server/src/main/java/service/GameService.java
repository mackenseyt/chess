package service;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class GameService {
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDao = new GameDAO();
    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        // Logic to create a new game
//        check auth token
        var game = new GameData(request.gameName());
        gameDao.createGame(game);

//        initialize game
        return new CreateGameResponse(game.getGameID());
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        var game = gameDao.getGame(request.gameID());
        if(game == null){
            throw new DataAccessException("Game not found");
        }
        var username = authDAO.getAuth(request.authToken()).getUsername();
        gameDao.claimGame(username, request.teamColor(), request.gameID());
    }
    public ListGameResponse listGames() throws DataAccessException {
        // Return a list of GameData objects
        ArrayList<GameData> games = new ArrayList<>(gameDao.listGames());
        return new ListGameResponse(games);
    }

}
