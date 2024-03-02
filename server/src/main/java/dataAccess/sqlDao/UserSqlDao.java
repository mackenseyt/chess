package dataAccess.sqlDao;


import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.UserData;

import java.sql.SQLException;

public class UserSqlDao{

    private final DatabaseManager db = new DatabaseManager();

    public UserSqlDao() throws DataAccessException{
        configureDatabase();
    }

    public void registerUser(String username, String password, String email) {

    }


    public UserData getUser(String username) {
        return null;
    }


    public void clear() {

    }


    public boolean containsUser(String username) {
        return false;
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
}
