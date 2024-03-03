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
        db.configureDatabase();
    }

    public void registerUser(String username, String password, String email) throws DataAccessException{
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashedPassword = encoder.encode(password);
        try {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            db.executeUpdate(statement, username, password, email);
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
        db.executeUpdate(statement);
    }

}
