package Handlers;

import Service.GameService;
import Service.UserService;
import dataAccess.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import spark.Response;

public class SessionHandler {
    private static final UserService userService = new UserService();
    public static void authoriseUser(String authToken, Response response) throws DataAccessException{
        try{
            userService.authoriseUser(authToken);
        }catch(DataAccessException e){
            response.status(401);
            throw e;
        }
    }
}
