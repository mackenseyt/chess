package service;
import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.GameSqlDao;
import dataAccess.sqlDao.UserSqlDao;


public class ApplicationService {

    private static final AuthSqlDao authDAO = new AuthSqlDao();
    private static final UserSqlDao userDAO = new UserSqlDao();
    private static final GameSqlDao gameDAO = new GameSqlDao();



    public void clearAllData(){
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
