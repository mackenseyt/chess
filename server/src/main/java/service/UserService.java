package service;

import dataAccess.sqlDao.AuthSqlDao;
import dataAccess.sqlDao.UserSqlDao;
import model.AuthData;
import dataAccess.DataAccessException;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;



public class UserService {
    private static final AuthSqlDao authDao;
    private static final UserSqlDao userDao;

    static {
        try {
            authDao = new AuthSqlDao();
            userDao = new UserSqlDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public LoginResponse register(RegisterRequest request) throws DataAccessException {
        if(userDao.containsUser(request.username())){
            throw new DataAccessException("Name in use");
        }
        userDao.registerUser(request.username(), request.password(), request.email());
        var authToken = new AuthData(request.username());
        authDao.addAuth(authToken);
        return new LoginResponse(request.username(), authToken.getAuthToken());
    }

    public LoginResponse login(LoginRequest request) throws  DataAccessException{
        Boolean user = userDao.containsUser(request.username());
        UserData userObj = userDao.getUser(request.username());
        if(!user || !userObj.getPassword().equals(request.password())){
            throw new DataAccessException("Username or password is incorrect");
        }
        var authToken = new AuthData(request.username());
        authDao.addAuth(authToken);
        return new LoginResponse(authToken.getUsername(), authToken.getAuthToken());

    }

    public void logout(String authToken)throws DataAccessException{
        authDao.deleteAuth(authToken);
    }

    public void authoriseUser(String authToken) throws DataAccessException{
        if(!authDao.containsAuth(authToken)){
            throw new DataAccessException("unauthorized");
        }
    }
}
