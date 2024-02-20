package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO {

    private static final Map<String, String> storage = new HashMap<>();

    public static String createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        storage.put(username, authToken);
        return authToken;
    }
    public static String getAuth(String token) throws DataAccessException{
        String auth = storage.get(token);
//            throw new DataAccessException("Not authorized");
        return auth;
    }

    public static void deleteAuth(String token) throws DataAccessException{
//        if(!storage.containsKey(token)){
//            throw new DataAccessException("Token does not exist");
//        }
        storage.remove(token);
    }

    public void clear() {
        storage.clear();
    }
}
