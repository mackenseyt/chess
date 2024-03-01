package dataAccess.sqlDao;

import dataAccess.UserDaoInterface;
import model.UserData;

public class UserSqlDao implements UserDaoInterface {

    private final String[] createStatement = {
            """
        CREATE TABLE IF NOT EXISTS user (
        'username' VARCHAR(255) NOT NULL,
        'password' VARCHAR(255) NOT NULL,
        'email' VARCHAR(255) NOT NULL,
        PRIMARY KEY (username),
        
        """
    };
    @Override
    public void registerUser(String username, String password, String email) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsUser(String username) {
        return false;
    }
}
