package dataAccess;

//createGame: Create a new game.
//getGame: Retrieve a specified game with the given game ID.
//listGames: Retrieve all games.
//updateGame: Updates a chess game. It should replace the chess game string corresponding to a given gameID.
//            This is used when players join a game or when a move is made.

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDAO{
    private static final Map<Integer, GameData> storage = new HashMap<>();
    private static int nextGameID = 1;

    public static Integer createGame(String gameName) throws DataAccessException{

        for (GameData gameData : storage.values()) {
            if (gameData.getGameName().equals(gameName)) {
                throw new DataAccessException("Game with name " + gameName + " already exists");
            }
        }

        int gameID = nextGameID++;
        GameData newGame = new GameData(gameID, null, null, gameName, null);
        storage.put(gameID, newGame);
        return gameID;
    }

    private GameData getGame(Integer id)throws DataAccessException{
        GameData game = storage.get(id);
        if(game == null){
            throw new DataAccessException("Game does not exist");
        }
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
}
