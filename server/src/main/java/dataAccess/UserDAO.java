package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDAO {

    private static final Map<String, UserData> userStoarage = new HashMap<>();
    private final Map<String, String> authTokens = new HashMap<>();


//create a new user
    public static void registerUser(String username, String password, String email) throws DataAccessException{
        if(userStoarage.containsKey(username)){
            throw new DataAccessException("Username already taken");
        }
        UserData user = new UserData(username, password, email);
        userStoarage.put(username, user);
    }
//    get user by username
    public static UserData getUser(String username) throws DataAccessException{
        return userStoarage.get(username);
    }

//    public String createAuthToken(String username) {
//        String authToken = UUID.randomUUID().toString();
//        authTokens.put(username, authToken);
//        return authToken;
//    }

    public void clear() {
        userStoarage.clear();
        authTokens.clear();
    }
//
//    public void deleteUser(String username) throws DataAccessException{
//        if(!userStoarage.containsKey(username)){
//            throw new DataAccessException("user does not exist");
//        }
//        userStoarage.remove(username);
//    }


}
