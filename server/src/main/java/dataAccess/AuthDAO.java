package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO {

    private static final HashMap<String, String> storage = new HashMap<>();

//    public AuthDAO(String username){
//        this.authToken = createAuth(username);
//        return authToken;
//    }

    public void addAuth(AuthData authToken){
        storage.put(authToken.getUsername(), authToken.getAuthToken());
    }
    public AuthData createAuth(String username){
        AuthData authToken = new AuthData(username);
        storage.put(authToken.getUsername(), authToken.getAuthToken());
        return authToken;
    }
    public boolean containsAuth(String token){
        return storage.containsValue(token);
    }
    public static AuthData getAuth(String token){
        for (Map.Entry<String, String> entry : storage.entrySet()) {
            if (entry.getValue().equals(token)) {
                return new AuthData(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }


    public static void deleteAuth(String token){
//        for (Map.Entry<String, String> entry : storage.entrySet()) {
//            if (entry.getValue().equals(token)) {
//                storage.remove(token);
//            }
//        }
        storage.remove(token);
    }

    public void clear() {
        storage.clear();
    }


}
