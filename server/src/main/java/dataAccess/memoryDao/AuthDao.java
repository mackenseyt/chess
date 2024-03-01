package dataAccess.memoryDao;

import dataAccess.AuthDaoInterface;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDao implements AuthDaoInterface {

    public static final HashMap<String, String> storage = new HashMap<>();


    public void addAuth(AuthData authToken){
        storage.put(authToken.getAuthToken(),authToken.getUsername());
    }

    public boolean containsAuth(String token){
        HashMap<String, String> map = this.storage;
        return storage.containsKey(token);
    }
    public AuthData getAuth(String token){
        return new AuthData(storage.get(token), token);
    }


    public void deleteAuth(String token){
        storage.remove(token);
    }

    public void clear() {
        storage.clear();
    }



}
