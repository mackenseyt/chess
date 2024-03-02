package service;
import dataAccess.DataAccessException;
import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.GameSqlDao;
import dataAccess.sqlDao.UserSqlDao;


public class ApplicationService {

    private static final AuthSqlDao authDao;
    private static final UserSqlDao userDao;
    private static final GameSqlDao gameDao;
    static {
        try {
            authDao = new AuthSqlDao();
            userDao = new UserSqlDao();
            gameDao = new GameSqlDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }




    public void clearAllData()throws DataAccessException{
        authDao.clear();
        userDao.clear();
        gameDao.clear();
    }
}
