package dataAccess;
import model.UserData;
import model.AuthData;

import java.util.Map;
public class Manage{
    private Map<String, UserData> userStorage;
    private Map<String, AuthData> authStorage;

    public Manage(Map<String, UserData> userStorage, Map<String, AuthData> authStorage) {
        this.userStorage = userStorage;
        this.authStorage = authStorage;
    }

    public void clearAllData() {
        userStorage.clear();
        authStorage.clear();
        // Add other data stores if necessary
    }
}
