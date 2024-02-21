package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.AuthData;
import request.LoginRequest;
import response.LoginResponse;
import service.ApplicationService;
import service.UserService;
import dataAccess.DataAccessException;
import spark.Response;

import java.util.Map;

public class SessionHandler {
    private static final UserService userService = new UserService();
    private final ApplicationService applicationService = new ApplicationService();

    public void login(Map<String, Object> body, Response response) throws DataAccessException{
        LoginRequest loginRequest = new Gson().fromJson(body.toString(), LoginRequest.class);
        try{
            LoginResponse loginResponse = userService.login(loginRequest);
            response.header("Authorization", loginResponse.authToken());
            response.body(new Gson().toJson(loginResponse));
        }catch(DataAccessException e){
            response.status(401);
            response.body(new Gson().toJson(Map.of("message", "Username or password is incorrect")));
        }
    }
    public void logout(String authToken, Response response) throws DataAccessException{
        userService.logout(authToken);
        response.status(200);
        response.body(new Gson().toJson(new JsonObject()));
    }

    public void clearDatabase(Response response)throws DataAccessException {
        applicationService.clearAllData();
        response.body(new Gson().toJson(Map.of("message", "Clear succeeded", "success", true)));
    }
}
