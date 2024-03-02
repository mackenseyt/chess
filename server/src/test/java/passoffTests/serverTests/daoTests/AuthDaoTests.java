package passoffTests.serverTests.daoTests;

import dataAccess.sqlDao.AuthSqlDao;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDaoTests {
    private static final AuthSqlDao authDao;

    static {
        try {
            authDao = new AuthSqlDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(authDao::clear);
    }

    @Test
    void testClear(){
//       put something into the table
        AuthData auth = new AuthData("testuser","testToken" );
        Assertions.assertDoesNotThrow(()-> authDao.addAuth(auth));
        Assertions.assertDoesNotThrow(()-> Assertions.assertNotNull(authDao.getAuth("testToken")));

//  clear it and make sure its empty
        Assertions.assertDoesNotThrow(authDao::clear);
        Assertions.assertDoesNotThrow(()-> Assertions.assertNull(authDao.getAuth("testToken")));
    }
    @Test
    void addAuth(){
        //        put an auth token and username in
        AuthData auth = new AuthData("testuser","testToken" );
        Assertions.assertDoesNotThrow(()-> authDao.addAuth(auth));

//        check if its there
        AuthData foundToken = Assertions.assertDoesNotThrow(()-> authDao.getAuth("testToken"));
        Assertions.assertEquals(auth, foundToken);
    }

    @Test
    void getAuth(){
        // Add an AuthData object to the database
        AuthData auth = new AuthData("testuser", "testToken");
        Assertions.assertDoesNotThrow(() -> authDao.addAuth(auth));

        // Retrieve the AuthData object using getAuth()
        AuthData retrievedAuth = Assertions.assertDoesNotThrow(() -> authDao.getAuth("testToken"));

        // Assert that the retrieved AuthData object matches the one added to the database
        Assertions.assertNotNull(retrievedAuth);
        Assertions.assertEquals(auth.getUsername(), retrievedAuth.getUsername());
        Assertions.assertEquals(auth.getAuthToken(), retrievedAuth.getAuthToken());
    }

    @Test
    void deleteAuth(){
        AuthData auth = new AuthData("testuser", "testToken");
        Assertions.assertDoesNotThrow(() -> authDao.addAuth(auth));

        Assertions.assertDoesNotThrow(() -> {
            authDao.deleteAuth("testToken");
            Assertions.assertNull(authDao.getAuth("testToken"));
        });
    }

}
