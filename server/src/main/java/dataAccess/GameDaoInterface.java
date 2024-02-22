package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDaoInterface {
    void createGame(GameData game) throws DataAccessException;
    GameData getGame(Integer id);
    ArrayList<GameData> listGames() throws DataAccessException;
    void clear();
    void claimGame(String username, ChessGame.TeamColor teamColor, int id) throws DataAccessException;
}
