package dataAccess.sqlDao;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import dataAccess.DatabaseManager;

import java.sql.SQLException;
import java.sql.*;
import static java.sql.Types.NULL;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthSqlDao{
    private final DatabaseManager db = new DatabaseManager();
    public AuthSqlDao() throws DataAccessException{
        configureDatabase();
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
    private final String[] createStatement = {
        """
        CREATE TABLE IF NOT EXISTS user (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        )""",
        """
        CREATE TABLE IF NOT EXISTS auth (
             authToken VARCHAR(255) NOT NULL,
             username VARCHAR(255) NOT NULL,
             PRIMARY KEY (authToken),
             FOREIGN KEY (username) REFERENCES user(username)
         )""",
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

    public void addAuth(AuthData authToken) throws DataAccessException {
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            executeUpdate(statement, authToken.getAuthToken(), authToken.getUsername());
        }catch(DataAccessException e){
            throw  new DataAccessException(e.toString());
        }
    }

    public AuthData getAuth(String token)throws DataAccessException {
        try (var conn = db.getConnection()) {
            var statement = "SELECT * FROM auth WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, token);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("username"), rs.getString("authToken"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
        return null;
    }

    public void deleteAuth(String token) throws DataAccessException{
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, token);
    }

    public void clear()throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }
}
