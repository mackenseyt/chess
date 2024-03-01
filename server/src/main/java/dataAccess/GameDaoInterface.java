package dataAccess;
import chess.ChessGame;
import dataAccess.memoryDao.GameDao;
import model.GameData;

import java.util.ArrayList;

public interface GameDaoInterface {
    GameData getGame(Integer id);
    void addGame(GameData game) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void clear();
    void claimGame(String username, ChessGame.TeamColor teamColor, int id) throws DataAccessException;
}
