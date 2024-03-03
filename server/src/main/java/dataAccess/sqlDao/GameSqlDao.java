package dataAccess.sqlDao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;

import dataAccess.DatabaseManager;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GameSqlDao{

    private final DatabaseManager db = new DatabaseManager();

    public GameSqlDao() throws DataAccessException{
        configureDatabase();
    }

    public void addGame(GameData game) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, game.getGameID());
                ps.setString(2, game.getWhiteUsername());
                ps.setString(3, game.getBlackUsername());
                ps.setString(4, game.getGameName());
                ps.setString(5, new Gson().toJson(game.getGame())); // Serialize ChessGame object to JSON
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(Integer id) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Retrieve data from the result set
                        int gameId = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String gameData = rs.getString("game"); // Assuming game is stored as a String in the database

                        ChessGame game = deserializeChessGame(gameData);

                        // Create and return the GameData object
                        return new GameData(gameId, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null; // Return null if no game with the specified ID is found
    }


    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = db.getConnection()) {
            var statement = "SELECT * FROM game";
            try (var ps = conn.prepareStatement(statement);
                 var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gameId = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String gameData = rs.getString("game"); // Assuming game is stored as a String in the database

                    ChessGame game = deserializeChessGame(gameData);

                    games.add(new GameData(gameId, whiteUsername, blackUsername, gameName, game));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
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
    private int executeUpdate(String statement, Object... params)throws DataAccessException{
        try (var conn = db.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
    public void clear() throws DataAccessException{
        var statement = "DELETE FROM game";
        executeUpdate(statement);
    }
    private ChessGame deserializeChessGame(String gameData) {
        return new Gson().fromJson(gameData, ChessGame.class);
    }


}
