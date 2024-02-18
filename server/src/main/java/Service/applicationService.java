package Service;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;


public class applicationService {

    private static final AuthDAO authDAO;
    private static final UserDAO userDAO;
    private static final GameDAO gameDAO;

    static {
        try {
            authDAO = new AuthDAO();
            userDAO = new UserDAO();
            gameDAO = new GameDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void clearAllData() throws DataAccessException{
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
