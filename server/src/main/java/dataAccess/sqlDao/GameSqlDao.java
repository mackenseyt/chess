package dataAccess.sqlDao;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDaoInterface;
import model.GameData;

import java.util.ArrayList;

public class GameSqlDao implements GameDaoInterface {


    private final String[] createStatement = {
            """
        CREATE TABLE IF NOT EXISTS game (
        
        """
    };
    public void addGame(GameData game) throws DataAccessException {

    }
    @Override
    public GameData getGame(Integer id) {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void claimGame(String username, ChessGame.TeamColor teamColor, int id) throws DataAccessException {

    }


}
