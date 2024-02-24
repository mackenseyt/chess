package passoffTests.serverTests;

import dataAccess.AuthDao;
import dataAccess.GameDao;
import dataAccess.UserDao;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ApplicationService;

class AdminServiceTest {

    private static final UserDao userDao = new UserDao();
    private static final AuthDao authTokenDao = new AuthDao();
    private static final GameDao gameDao = new GameDao();
    private static final ApplicationService testAdmin = new ApplicationService();

    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authTokenDao::clear);
        Assertions.assertTrue(userDao.userStorage.isEmpty());
        Assertions.assertTrue(authTokenDao.storage.isEmpty());
        Assertions.assertTrue(gameDao.storage.isEmpty());
    }

    @Test
    @DisplayName("Clear Test")
    public void clearDatabase() {

        // add some data to the database
        UserData testUser = new UserData("testUser", "password", "test@test.com");
        Assertions.assertDoesNotThrow(() -> userDao.registerUser(testUser.getUsername(), testUser.getPassword(), testUser.getEmail()));
        AuthData testToken = new AuthData("testUser");
        Assertions.assertDoesNotThrow(() -> authTokenDao.addAuth(testToken));
        GameData testGame = new GameData("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.addGame(testGame));

        // check the database is not empty
        Assertions.assertFalse(userDao.userStorage.isEmpty());
        Assertions.assertFalse(authTokenDao.storage.isEmpty());
        Assertions.assertFalse(gameDao.storage.isEmpty());

        Assertions.assertDoesNotThrow(testAdmin::clearAllData);

        // check that the database is clear
        Assertions.assertTrue(userDao.userStorage.isEmpty());
        Assertions.assertTrue(authTokenDao.storage.isEmpty());
        Assertions.assertTrue(gameDao.storage.isEmpty());
    }
}

