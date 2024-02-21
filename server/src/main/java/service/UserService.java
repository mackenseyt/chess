package service;

import model.AuthData;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAO;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;



public class UserService {
    private static final AuthDAO authDAO = new AuthDAO();
    private static final UserDAO userDAO = new UserDAO();

//    public UserService(UserDAO userDao){
//        userDAO = userDao;
//    }
//    static {
//        try {
//            authDAO = new AuthDAO();
//            userDAO = new UserDAO();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    public static LoginResponse register(RegisterRequest request) throws DataAccessException {
        if(userDAO.getUser(request.username())!= null){
            return null;
        }
        userDAO.registerUser(request.username(), request.password(), request.email());
        var authToken = new AuthData(request.username());
        return new LoginResponse(request.username(), authToken.getAuthToken());
    }

    public LoginResponse login(LoginRequest request) throws  DataAccessException{
        UserData user = userDAO.getUser(request.username());
        if(user == null || !user.getPassword().equals(request.password())){
            throw new DataAccessException("Username or password is incorrect");
        }
        var authToken = new AuthData(request.username());
//        var username = authData.getUsername();
        authDAO.addAuth(authToken);
        return new LoginResponse(authToken.getUsername(), authToken.getAuthToken());

    }

    public void logout(String authToken)throws DataAccessException{
        authDAO.deleteAuth(authToken);
    }

    public void authoriseUser(String authToken) throws DataAccessException{
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("unauthorized");
        }
    }
}
