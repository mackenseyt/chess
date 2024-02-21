package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    private static final Map<String, UserData> userStorage = new HashMap<>();


//create a new user
    public static void registerUser(String username, String password, String email){
//        if(userStorage.containsKey(username)){
//            throw new DataAccessException("Username already taken");
//        }
        UserData user = new UserData(username, password, email);
        userStorage.put(username, user);
    }
//    get user by username
    public UserData getUser(String username){
        return userStorage.get(username);
    }

//    public String createAuthToken(String username) {
//        String authToken = UUID.randomUUID().toString();
//        authTokens.put(username, authToken);
//        return authToken;
//    }

    public void clear() {
        userStorage.clear();
//        authTokens.clear();
    }
//
//    public void deleteUser(String username) throws DataAccessException{
//        if(!userStoarage.containsKey(username)){
//            throw new DataAccessException("user does not exist");
//        }
//        userStoarage.remove(username);
//    }


}
