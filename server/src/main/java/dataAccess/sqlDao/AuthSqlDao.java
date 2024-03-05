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
        var conn = db.getConnection();
        try {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            db.executeUpdate(conn, statement, authToken.getAuthToken(), authToken.getUsername());
        }catch(DataAccessException e){
            throw  new DataAccessException(e.toString());
        }finally {
            db.closeConnection(conn);
        }
    }

    public AuthData getAuth(String token)throws DataAccessException {
        var conn = db.getConnection();
        try {
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
        }finally {
            db.closeConnection(conn);
        }
        return null;
    }

    public void deleteAuth(String token) throws DataAccessException{
        var conn = db.getConnection();
        var statement = "DELETE FROM auth WHERE authToken=?";
        db.executeUpdate(conn, statement, token);
        db.closeConnection(conn);
    }

    public void clear()throws DataAccessException {
        var conn = db.getConnection();
        var statement = "TRUNCATE auth";
        db.executeUpdate(conn, statement);
        db.closeConnection(conn);
    }
}
