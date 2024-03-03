package dataAccessTests;

import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.GameSqlDao;
import dataAccess.sqlDao.UserSqlDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoTests {
    private static final UserSqlDao userDao;
    private static final GameSqlDao gameDao;
    static {
        try {
            userDao = new UserSqlDao();
            gameDao = new GameSqlDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
    }
    @Test
    void testClear(){
//        UserData user = new UserData("testuser","testPassword", "testEmail");
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testuser","testPassword", "testEmail"));
        Assertions.assertDoesNotThrow(()-> Assertions.assertNotNull(userDao.getUser("testUser")));

//  clear it and make sure its empty
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(()-> Assertions.assertNull(userDao.getUser("testUsername")));
    }

    @Test
    void registerUser(){
        UserData user = new UserData("testUser", "testPass", "testEmail");
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testUser", "testPass", "testEmail"));

//        check if its there
        UserData foundUser = Assertions.assertDoesNotThrow(()-> userDao.getUser("testUser"));
        Assertions.assertEquals(user, foundUser);

    }

    @Test
    void getUser(){
        UserData user = new UserData("testUser", "testPass", "testEmail");
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testUser", "testPass", "testEmail"));

        UserData retrievedUser= Assertions.assertDoesNotThrow(() -> userDao.getUser("testUser"));

        // Assert that the retrieved AuthData object matches the one added to the database
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(user.getUsername(), retrievedUser.getUsername());
        Assertions.assertEquals(user.getPassword(), retrievedUser.getPassword());
        Assertions.assertEquals(user.getEmail(), retrievedUser.getEmail());
    }
}
