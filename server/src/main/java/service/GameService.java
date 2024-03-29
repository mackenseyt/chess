package service;
import dataAccess.DataAccessException;
import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.GameSqlDao;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;

import java.util.ArrayList;

public class GameService {
    private static final AuthSqlDao authDao;
    private static final GameSqlDao gameDao;
    static {
        try {
            authDao = new AuthSqlDao();
            gameDao = new GameSqlDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        // Logic to create a new game
//        check auth token
        var game = new GameData(request.gameName());
        gameDao.addGame(game);

//        initialize game
        return new CreateGameResponse(game.getGameID());
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        var game = gameDao.getGame(request.gameID());
        if(game == null){
            throw new DataAccessException("Game not found");
        }
        var username = authDao.getAuth(request.authToken()).getUsername();
        gameDao.claimGame(username, request.playerColor(), request.gameID());
    }
    public ListGameResponse listGames() throws DataAccessException {
        // Return a list of GameData objects
        ArrayList<GameData> games = new ArrayList<>(gameDao.listGames());
        return new ListGameResponse(games);
    }


}
