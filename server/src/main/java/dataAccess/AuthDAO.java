package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO {

    private final Map<String, AuthData> storage = new HashMap<>();

    private void createAuth(AuthData auth)throws DataAccessException{
        if(storage.containsKey(auth.getAuthToken())){
            throw new DataAccessException("Authorization already exists");
        }
        storage.put(auth.getAuthToken(), auth);
    }
    private AuthData getAuth(String token) throws DataAccessException{
        AuthData auth = storage.get(token);
        if(auth == null){
            throw new DataAccessException("Authorization does not exist");
        }
        return auth;
    }

    private void deleteAuth(String token) throws DataAccessException{
        if(!storage.containsKey(token)){
            throw new DataAccessException("Token does not exist");
        }
        storage.remove(token);
    }

    public void clear() {
        storage.clear();
    }
}
