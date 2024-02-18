package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDAO {

    private final Map<String, UserData> userStoarage = new HashMap<>();
    private final Map<String, String> authTokens = new HashMap<>();
//create a new user
    public void registerUser(String username, String password, String email) throws DataAccessException{
        if(userStoarage.containsKey(username)){
            throw new DataAccessException("Username already exists");
        }
        UserData user = new UserData(username, password, email);
        userStoarage.put(username, user);
    }
//    get user by username
    public UserData getUser(String username) throws DataAccessException{
        UserData user = userStoarage.get(username);
        if(user == null){
            throw new DataAccessException("username does not exist");
        }
        return user;
    }
    public String createAuthToken(String username) {
        String authToken = UUID.randomUUID().toString();
        authTokens.put(username, authToken);
        return authToken;
    }
//
//    public void deleteUser(String username) throws DataAccessException{
//        if(!userStoarage.containsKey(username)){
//            throw new DataAccessException("user does not exist");
//        }
//        userStoarage.remove(username);
//    }

}
