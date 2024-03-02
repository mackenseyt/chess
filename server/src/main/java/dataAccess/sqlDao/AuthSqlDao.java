package dataAccess.sqlDao;

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
        CREATE TABLE IF NOT EXISTS auth (
             authToken VARCHAR(255) NOT NULL,
             username VARCHAR(255) NOT NULL,
             PRIMARY KEY (authToken),
             FOREIGN KEY (username) REFERENCES user(username)
         )"""
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

    public void addAuth(AuthData authToken) {

    }

    public boolean containsAuth(String token) {
        return false;
    }

    public AuthData getAuth(String token) {
        return null;
    }

    public void deleteAuth(String token) {

    }

    public void clear()throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }
}
