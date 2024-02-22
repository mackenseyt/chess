package service;
import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;


public class ApplicationService {

    private static final AuthDao authDAO;
    private static final UserDao userDAO;
    private static final GameDao gameDAO;

    static {
        try {
            authDAO = new AuthDao();
            userDAO = new UserDao();
            gameDAO = new GameDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void clearAllData() throws DataAccessException{
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
