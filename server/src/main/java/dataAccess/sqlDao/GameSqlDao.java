package dataAccess.sqlDao;

import chess.ChessGame;
import dataAccess.DataAccessException;

import dataAccess.DatabaseManager;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameSqlDao{

    private final DatabaseManager db = new DatabaseManager();

    public GameSqlDao() throws DataAccessException{
        configureDatabase();
    }

    public void addGame(GameData game) throws DataAccessException {

    }

    public GameData getGame(Integer id) {
        return null;
    }


    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }


    public void clear() {

    }


    public void claimGame(String username, ChessGame.TeamColor teamColor, int id) throws DataAccessException {

    }
    private final String[] createStatement = {
            """
        CREATE TABLE IF NOT EXISTS game (
             gameID INT NOT NULL,
             whiteUsername VARCHAR(255),
             blackUsername VARCHAR(255),
             gameName VARCHAR(255) NOT NULL,
             game LONGTEXT NOT NULL,
             PRIMARY KEY (gameID),
             FOREIGN KEY (whiteUsername) REFERENCES user(username),
             FOREIGN KEY (blackUsername) REFERENCES user(username)
         )
         """
    };
    private void configureDatabase() throws DataAccessException{
        db.createDatabase();
        try(var conn = db.getConnection()){
            for(var state: createStatement){
                try(var preparedStatement = conn.prepareStatement(state)){
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

}
