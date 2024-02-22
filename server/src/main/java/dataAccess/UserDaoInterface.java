package dataAccess;

import model.UserData;

public interface UserDaoInterface {
    void registerUser(String username, String password, String email);
    UserData getUser(String username);
    void clear();
    boolean containsUser(String username);

}
