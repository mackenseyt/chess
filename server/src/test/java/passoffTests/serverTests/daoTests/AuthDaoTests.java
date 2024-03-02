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
        AuthData auth = new AuthData("testuser","testToken" );
    }
//    private AuthSqlDao getDataAccess(Class<? extends dataAccess> databaseClass) throws DataAccessException {
//        AuthSqlDao db = new AuthSqlDao();
////        if (databaseClass.equals(AuthSqlDao.class)) {
////            db = new AuthSqlDao();
////        } else {
////            db = new AuthDao();
////        }
//        db.clear();
//        return db;
//    }

}
