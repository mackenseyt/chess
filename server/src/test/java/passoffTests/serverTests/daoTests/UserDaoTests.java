package passoffTests.serverTests.daoTests;

import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.UserSqlDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoTests {
    private static final UserSqlDao userDao;
    static {
        try {
            userDao = new UserSqlDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @BeforeEach
    void setup() {
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
}
