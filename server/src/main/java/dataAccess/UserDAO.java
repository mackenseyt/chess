package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    private final Map<String, UserData> storage = new HashMap<>();

//create a new user
    private void createUser(UserData user) throws DataAccessException{
        if(storage.containsKey(user.getUsername())){
            throw new DataAccessException("Username already exists");
        }
        storage.put(user.getUsername(), user);
    }
//    get a user by their username
    private UserData getUser(String username) throws DataAccessException{
        UserData user = storage.get(username);
        if(user == null){
            throw new DataAccessException("username does not exist");
        }
        return user;
    }

    private void deleteUser(String username) throws DataAccessException{
        if(!storage.containsKey(username)){
            throw new DataAccessException("user does not exist");
        }
        storage.remove(username);
    }

}
