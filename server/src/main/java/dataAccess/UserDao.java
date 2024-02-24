package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static final Map<String, UserData> userStorage = new HashMap<>();


//create a new user
    public void registerUser(String username, String password, String email){
        UserData user = new UserData(username, password, email);
        userStorage.put(user.getUsername(), user);
    }
    public void addUser(UserData user){
        userStorage.put(user.getUsername(), user);
    }

    public UserData getUser(String username){
        return userStorage.get(username);
    }

    public void clear() {
        userStorage.clear();
    }

    public boolean containsUser(String username) {
        return userStorage.containsKey(username);
    }

    public boolean isClear(){
        return userStorage.isEmpty();
    }


}
