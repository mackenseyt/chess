package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.GameSqlDao;
import dataAccess.sqlDao.UserSqlDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        Assertions.assertEquals(user.getUsername(), foundUser.getUsername());

    }
    @Test
    void registerUserFail() {
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("existingUser", "testPass", "testEmail"));
        // Attempt to register a user with existing username
        Assertions.assertThrows(DataAccessException.class, () -> userDao.registerUser("existingUser", "testPass", "testEmail"));

        // Attempt to register a user with invalid input (e.g., null username)
        Assertions.assertThrows(DataAccessException.class, () -> userDao.registerUser(null, "testPass", "testEmail"));
    }


    @Test
    void getUser(){

        UserData user = new UserData("testUser", "testPass", "testEmail");
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testUser", "testPass", "testEmail"));

        UserData retrievedUser= Assertions.assertDoesNotThrow(() -> userDao.getUser("testUser"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Assert that the retrieved AuthData object matches the one added to the database
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(user.getUsername(), retrievedUser.getUsername());
        Assertions.assertTrue(encoder.matches(user.getPassword(), retrievedUser.getPassword()));
        Assertions.assertEquals(user.getEmail(), retrievedUser.getEmail());
    }
    @Test
    void getUserFail() {
        // Attempt to retrieve a non-existing user
        Assertions.assertDoesNotThrow(() -> userDao.getUser("nonExistingUser"));
    }

}
