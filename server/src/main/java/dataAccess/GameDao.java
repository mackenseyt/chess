package dataAccess;

//createGame: Create a new game.
//getGame: Retrieve a specified game with the given game ID.
//listGames: Retrieve all games.
//updateGame: Updates a chess game. It should replace the chess game string corresponding to a given gameID.
//            This is used when players join a game or when a move is made.

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameDao {
    private static final Map<Integer, GameData> storage = new HashMap<>();


    public void addGame(GameData game) throws DataAccessException{
        for (GameData gameData : storage.values()) {
            if (game.getGameName().equals(gameData.getGameName())) {
                throw new DataAccessException("Game with name " + gameData.getGameName() + " already exists");
            }
        }
        storage.put(game.getGameID(), game);
//        return game.getGameID();
    }

    public GameData getGame(Integer id){
        GameData game = storage.get(id);
        return game;
    }

    public ArrayList<GameData> listGames() throws DataAccessException{
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

    public void claimGame(String username, ChessGame.TeamColor teamColor, int i) throws DataAccessException{
        GameData game = getGame(i);
//        user can watch
        if(teamColor == null) return;
        switch (teamColor){
            case WHITE:
                if(game.getWhiteUsername() == null){
                    game.setWhiteUsername(username);
                }else{
                    throw new DataAccessException("already taken");
                }
                break;
            case BLACK:
                if(game.getBlackUsername() == null){
                    game.setBlackUsername(username);
                }else{
                    throw new DataAccessException("already taken");
                }
                break;
        }
        updateGame(game);
//        GameData game = new GameData(gameID, teamColor, username);
    }
    public boolean isClear(){
        return storage.isEmpty();
    }
}
