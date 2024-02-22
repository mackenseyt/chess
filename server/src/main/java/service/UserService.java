package service;

import model.AuthData;
import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDao;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;



public class UserService {
    private static final AuthDao authDAO = new AuthDao();
    private static final UserDao userDAO = new UserDao();

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
    public LoginResponse register(RegisterRequest request) throws DataAccessException {
        if(userDAO.containsUser(request.username())){
            throw new DataAccessException("Name in use");
        }
        userDAO.registerUser(request.username(), request.password(), request.email());
        var authToken = new AuthData(request.username());
        authDAO.addAuth(authToken);
        return new LoginResponse(request.username(), authToken.getAuthToken());
    }

    public LoginResponse login(LoginRequest request) throws  DataAccessException{
        Boolean user = userDAO.containsUser(request.username());
        UserData userObj = userDAO.getUser(request.username());
        if(!user || !userObj.getPassword().equals(request.password())){
            throw new DataAccessException("Username or password is incorrect");
        }
        var authToken = new AuthData(request.username());
        authDAO.addAuth(authToken);
        return new LoginResponse(authToken.getUsername(), authToken.getAuthToken());

    }

    public void logout(String authToken)throws DataAccessException{
        authDAO.deleteAuth(authToken);
    }

    public void authoriseUser(String authToken) throws DataAccessException{
        if(!authDAO.containsAuth(authToken)){
            throw new DataAccessException("unauthorized");
        }
    }
}
