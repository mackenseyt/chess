package dataAccess;
import model.GameData;
import model.UserData;
import model.AuthData;

import java.util.Map;
public class Manage{
    private final Map<String, UserData> userStorage;
    private final Map<String, AuthData> authStorage;

    private final Map<Integer, GameData> gameStorage;

    public Manage(Map<String, UserData> userStorage, Map<String, AuthData> authStorage, Map<Integer, GameData> gameStorage) {
        this.userStorage = userStorage;
        this.authStorage = authStorage;
        this.gameStorage = gameStorage;
    }

    public void clearAllData() {
        userStorage.clear();
        authStorage.clear();
        gameStorage.clear();

    }
}
