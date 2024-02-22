package dataAccess;
import model.AuthData;
public interface AuthDaoInterface {
    void addAuth(AuthData authToken);
    AuthData createAuth(String username);
    boolean containsAuth(String token);
    AuthData getAuth(String token);
    void deleteAuth(String token);
    void clear();
}
