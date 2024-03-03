package dataAccess.sqlDao;


import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserSqlDao{

    private final DatabaseManager db = new DatabaseManager();

    public UserSqlDao() throws DataAccessException{
        configureDatabase();
    }

    public void registerUser(String username, String password, String email) throws DataAccessException{
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashedPassword = encoder.encode(password);
        try {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            executeUpdate(statement, username, password, email);
        }catch(DataAccessException e){
            throw  new DataAccessException(e.toString());
        }
    }


    public UserData getUser(String username) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var statement = "SELECT * FROM user WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
        return null;
    }


    public void clear() throws DataAccessException {
        var statement = "DELETE FROM user";
        executeUpdate(statement);
    }


    private final String[] createStatement = {
            """
        CREATE TABLE IF NOT EXISTS user (
             username VARCHAR(255) NOT NULL,
             password VARCHAR(255) NOT NULL,
             email VARCHAR(255) NOT NULL,
             PRIMARY KEY (username)
         )"""
    };

    private void configureDatabase() throws DataAccessException {
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
}
