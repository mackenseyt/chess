package Service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAO;


public class UserService {
    private static final UserDAO userDao = new UserDAO();

//    public UserService(UserDAO userDao){
//        userDAO = userDao;
//    }
    public static String register(String username, String password, String email) throws DataAccessException {
        if(userDao.getUser(username)!= null){
            return null;
        }
        UserDAO.registerUser(username, password, email);
        String authToken = AuthDAO.createAuth(username);
        return authToken;
    }

    public static String login(String username, String password) throws  DataAccessException{
        UserData user = UserDAO.getUser(username);
        if(user != null && password.equals(user.getPassword())){
            String authToken = AuthDAO.createAuth(username);
            return authToken;
        }
        return null;
//        else{
////            response.status(400);
//            throw new DataAccessException("unauthorized ");
//        }
    }

    public static int logout(String authToken)throws DataAccessException{
        String auth = AuthDAO.getAuth(authToken);
        if(auth == null){
            return 0;
        }
        AuthDAO.deleteAuth(authToken);
        return 1;
    }

    public static void authoriseUser(String authToken) throws DataAccessException{
        if(AuthDAO.getAuth(authToken) == null){
            throw new DataAccessException("unauthorized");
        }
    }
}
