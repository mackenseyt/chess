package Service;

import model.AuthData;
import model.UserData;

public class userService {
    public static AuthData register(String username, String password, String email){
        throw new RuntimeException("Not implemented");
    }

    public AuthData login(UserData user){throw new RuntimeException("not written");}

    public void logout(String authToken){throw new RuntimeException("not written");}

}
