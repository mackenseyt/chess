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
        db.configureDatabase();
    }

    public void addAuth(AuthData authToken) throws DataAccessException {
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            db.executeUpdate(statement, authToken.getAuthToken(), authToken.getUsername());
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
        db.executeUpdate(statement, token);
    }

    public void clear()throws DataAccessException {
        var statement = "TRUNCATE auth";
        db.executeUpdate(statement);
    }
}
