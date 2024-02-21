package dataAccess;

//createGame: Create a new game.
//getGame: Retrieve a specified game with the given game ID.
//listGames: Retrieve all games.
//updateGame: Updates a chess game. It should replace the chess game string corresponding to a given gameID.
//            This is used when players join a game or when a move is made.

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDAO{
    private static final Map<Integer, GameData> storage = new HashMap<>();


    public static Integer createGame(GameData game) throws DataAccessException{
        for (GameData gameData : storage.values()) {
            if (game.getGameName().equals(gameData.getGameName())) {
                throw new DataAccessException("Game with name " + gameData.getGameName() + " already exists");
            }
        }
        storage.put(game.getGameID(), game);
        return game.getGameID();
    }

    public GameData getGame(Integer id){
        GameData game = storage.get(id);
        return game;
    }

    public static ArrayList<GameData> listGames() throws DataAccessException{
//        check auth tokens
        return new ArrayList<>(storage.values());
    }

    private void updateGame(GameData game) throws DataAccessException{
        if(!storage.containsKey(game.getGameID())){
            throw new DataAccessException("game does not exist");
        }
        storage.put(game.getGameID(), game);
    }

    public void clear() {
        storage.clear();
    }

    public void claimGame(String username, ChessGame.TeamColor teamColor, int i) {
        var game = getGame(i);
//        user can watch
        if(teamColor == null) return;
        throw new RuntimeException("not written");
    }
}
