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
        db.configureDatabase();
    }

    public void addGame(GameData game) throws DataAccessException {
        var conn = db.getConnection();
        try{
            var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
            db.executeUpdate(conn, statement, game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName(), new Gson().toJson(game.getGame()));
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }finally{
            db.closeConnection(conn);
        }
    }

    public GameData getGame(Integer id) throws DataAccessException {
        var conn = db.getConnection();
        try {
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
        }finally{
            db.closeConnection(conn);
        }
        return null; // Return null if no game with the specified ID is found
    }

    public void clear() throws DataAccessException{
        var conn = db.getConnection();
        var statement = "DELETE FROM game";
        db.executeUpdate(conn, statement);
        db.closeConnection(conn);
    }
    private ChessGame deserializeChessGame(String gameData) {
        return new Gson().fromJson(gameData, ChessGame.class);
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        var conn = db.getConnection();
        ArrayList<GameData> games = new ArrayList<>();
        try{
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
        }finally{
            db.closeConnection(conn);
        }
        return games;
    }


    public void claimGame(String username, ChessGame.TeamColor teamColor, int id) throws DataAccessException {
        var conn = db.getConnection();
        GameData game = getGame(id);
        if (teamColor == null) {
            return; // User can only watch
        }
        switch (teamColor) {
            case WHITE:
                if (game.getWhiteUsername() == null) {
                    game.setWhiteUsername(username);
                } else {
                    throw new DataAccessException("White side is already taken");
                }
                break;
            case BLACK:
                if (game.getBlackUsername() == null) {
                    game.setBlackUsername(username);
                } else {
                    throw new DataAccessException("Black side is already taken");
                }
                break;
        }
//         Update the specific columns in the database

//        these two lines of code make everything else break but I cant figure out why
        var statement = "UPDATE game SET whiteUsername = ?, blackUsername = ? WHERE gameID = ?";
        db.executeUpdate(conn, statement, game.getWhiteUsername(), game.getBlackUsername(), id);

//        db.executeUpdate(conn, "UPDATE game SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?",
//                game.getWhiteUsername(), game.getBlackUsername(), new Gson().toJson(game), game.getGameID());
        db.closeConnection(conn);
    }

}
