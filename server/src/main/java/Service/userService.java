package Service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataAccess.UserDAO;

public class userService {
    private static UserDAO userDAO = null;

    public userService(UserDAO userDao){
        this.userDAO = userDao;
    }
    public static String register(String username, String password, String email) throws DataAccessException {
        if(userDAO.getUser(username)!= null){
            return null;
        }
        userDAO.registerUser(username, password, email);
        String authToken = userDAO.createAuthToken(username);
        return authToken;
    }

    public AuthData login(UserData user){throw new RuntimeException("not written");}

    public void logout(String authToken){throw new RuntimeException("not written");}

}
