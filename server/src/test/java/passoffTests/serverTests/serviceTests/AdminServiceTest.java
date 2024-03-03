package passoffTests.serverTests.serviceTests;

import dataAccess.DataAccessException;
import dataAccess.memoryDao.AuthDao;
import dataAccess.memoryDao.GameDao;
import dataAccess.memoryDao.UserDao;
import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.GameSqlDao;
import dataAccess.sqlDao.UserSqlDao;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ApplicationService;

class AdminServiceTest {

    static final GameSqlDao gameDao;
    static final UserSqlDao userDao;
    static final AuthSqlDao authTokenDao;

    static {
        try {
            gameDao = new GameSqlDao();
            userDao = new UserSqlDao();
            authTokenDao = new AuthSqlDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private static final ApplicationService testAdmin = new ApplicationService();

    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authTokenDao::clear);
//        Assertions.assertTrue(userDao.userStorage.isEmpty());
//        Assertions.assertTrue(authTokenDao.storage.isEmpty());
//        Assertions.assertTrue(gameDao.storage.isEmpty());
    }

    @Test
    @DisplayName("Clear Test")
    public void clearDatabase() throws DataAccessException {

        // add some data to the database
        UserData testUser = new UserData("testUser", "password", "test@test.com");
        Assertions.assertDoesNotThrow(() -> userDao.registerUser(testUser.getUsername(), testUser.getPassword(), testUser.getEmail()));
        AuthData testToken = new AuthData("testUser");
        Assertions.assertDoesNotThrow(() -> authTokenDao.addAuth(testToken));
        GameData testGame = new GameData("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.addGame(testGame));

        // check the database is not empty
        Assertions.assertNotNull(userDao.getUser("testUser"));
        Assertions.assertNotNull(authTokenDao.getAuth(testToken.getAuthToken()));
        Assertions.assertNotNull(gameDao.getGame(testGame.getGameID()));

        Assertions.assertDoesNotThrow(testAdmin::clearAllData);

        // check that the database is clear
        Assertions.assertNull(userDao.getUser("testUser"));
        Assertions.assertNull(authTokenDao.getAuth(testToken.getAuthToken()));
        Assertions.assertNull(gameDao.getGame(testGame.getGameID()));
    }
}

