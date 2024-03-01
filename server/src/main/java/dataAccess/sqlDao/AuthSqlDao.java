package dataAccess.sqlDao;

import dataAccess.AuthDaoInterface;
import dataAccess.DataAccessException;
import model.AuthData;
import dataAccess.DatabaseManager;

import java.sql.SQLException;

public class AuthSqlDao implements AuthDaoInterface{
//    private final DatabaseManager db = new DatabaseManager();
    public AuthSqlDao() throws DataAccessException{
        configureDatabase();
    }
    private final String[] createStatement = {
        """
        CREATE TABLE IF NOT EXISTS auth (
        'authToken' VARCHAR(255) NOT NULL,
        'username' VARCHAR(255) NOT NULL,
        PRIMARY KEY (authToken),
        FOREIGN KEY (username) REFERENCES user(username)
        """
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var state: createStatement){

            }

        }
        catch(SQLException e){
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }

    }

    @Override
    public void addAuth(AuthData authToken) {

    }

    @Override
    public boolean containsAuth(String token) {
        return false;
    }

    @Override
    public AuthData getAuth(String token) {
        return null;
    }

    @Override
    public void deleteAuth(String token) {

    }

    @Override
    public void clear() {

    }
}
