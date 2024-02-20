package Handlers;
import Service.GameService;
import Service.UserService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;

import spark.Response;

import java.util.Map;

public class GameHandler {
    private static final GameService gameService = new GameService();
    public static void joinGame(Map<String, Object> bodyObj, String authToken, Response response) throws DataAccessException {

        try {

            int user = UserService.logout(authToken);
//            if(user == 0){
//                response.status(401);
//                return "{ \"message\": \"Error: unauthorized\" }";
//            }

        } catch (Exception e) {
            response.status(500);
        }
        response.body(new Gson().toJson(Map.of("message", "success")));
    }
}
