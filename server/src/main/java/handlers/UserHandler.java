package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.LoginResponse;
import spark.Response;
import service.UserService;

import java.util.Map;

public class UserHandler {
    private final UserService userService = new UserService();
    public void authorizeUser(String authToken, Response response) throws DataAccessException {
        try{
            userService.authoriseUser(authToken);

        }catch(DataAccessException e){
            response.status(401);
            throw e;
        }
    }
    public void registerUser(Map<String, Object> body, Response response)throws DataAccessException{
        RegisterRequest registerRequest = new Gson().fromJson(body.toString(), RegisterRequest.class);
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            response.status(400);
            throw new DataAccessException("bad request");
        }
        try {
            LoginResponse loginResponse = userService.register(registerRequest);
            response.body(new Gson().toJson(loginResponse));

        } catch (DataAccessException e) {
            // username/password already taken
            response.status(403);
            throw e;
        }
    }
}
