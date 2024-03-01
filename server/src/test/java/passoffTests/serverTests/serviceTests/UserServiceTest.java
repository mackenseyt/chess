package passoffTests.serverTests.serviceTests;

import dataAccess.memoryDao.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.memoryDao.GameDao;
import dataAccess.memoryDao.UserDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import service.UserService;

public class UserServiceTest {
    static final GameDao gameDao = new GameDao();
    static final UserDao userDao = new UserDao();
    static final AuthDao authDao = new AuthDao();
    static final UserService service = new UserService();
    static final UserData testUser = new UserData("testUser", "testPassword", "test@test.com");
    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authDao::clear);

        Assertions.assertTrue(userDao.userStorage.isEmpty());
        Assertions.assertTrue(authDao.storage.isEmpty());
        Assertions.assertTrue(gameDao.storage.isEmpty());
    }

    @Test
    @DisplayName("Successful register")
    void register(){
        RegisterRequest registerRequest = new RegisterRequest(testUser.getUsername(), testUser.getPassword(), testUser.getEmail());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(()-> service.register(registerRequest));

        Assertions.assertEquals(testUser.getUsername(), loginResponse.username());
        Assertions.assertEquals(testUser.getUsername(), Assertions.assertDoesNotThrow(()-> authDao.getAuth(loginResponse.authToken()).getUsername()));
        Assertions.assertEquals(testUser, Assertions.assertDoesNotThrow(()-> userDao.getUser(testUser.getUsername())));

    }

    @Test
    @DisplayName("register fail")
    void registerFail(){
        RegisterRequest registerRequest = new RegisterRequest(testUser.getUsername(), testUser.getPassword(), testUser.getEmail());
        Assertions.assertDoesNotThrow(()-> service.register(registerRequest));
        Assertions.assertThrows(DataAccessException.class, ()-> service.register(registerRequest));
    }

    @Test
    @DisplayName("Successful login")
    void login(){

        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testUser", "testPassword", "test@test.com"));

        LoginRequest loginRequest = new LoginRequest(testUser.getUsername(), testUser.getPassword());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(()-> service.login(loginRequest));

        Assertions.assertEquals(testUser.getUsername(), loginResponse.username());
        Assertions.assertEquals(Assertions.assertDoesNotThrow(()-> authDao.getAuth(loginResponse.authToken()).getAuthToken()),loginResponse.authToken());
        Assertions.assertEquals(Assertions.assertDoesNotThrow(()-> authDao.getAuth(loginResponse.authToken()).getUsername()), testUser.getUsername());
    }

    @Test
    @DisplayName("login fail")
    void loginFail(){
        LoginRequest loginRequest = new LoginRequest("invalidUser", "invlaidPassword");

        Assertions.assertNull(Assertions.assertDoesNotThrow(()-> userDao.getUser(loginRequest.username())));
        Assertions.assertThrows(DataAccessException.class,()-> service.login(loginRequest));
    }

    @Test
    @DisplayName("Successful logout")
    void logout(){
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testUser", "testPassword", "test@test.com"));


        LoginRequest loginRequest = new LoginRequest(testUser.getUsername(), testUser.getPassword());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(()-> service.login(loginRequest));

        Assertions.assertNotNull(Assertions.assertDoesNotThrow(() -> authDao.getAuth(loginResponse.authToken())));
        var correctToken = loginResponse.authToken();

        Assertions.assertDoesNotThrow(() -> service.logout("invalidAuthToken"));

        Assertions.assertNotNull(Assertions.assertDoesNotThrow(() -> authDao.getAuth(correctToken)));
    }

    @Test
    @DisplayName("logout fail")
    void logoutFail(){
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testUser", "testPassword", "test@test.com"));


        LoginRequest loginRequest = new LoginRequest(testUser.getUsername(), testUser.getPassword());
        LoginResponse loginResponse = Assertions.assertDoesNotThrow(() -> service.login(loginRequest));

        // check that the correct authToken is in the DAO
        Assertions.assertNotNull(Assertions.assertDoesNotThrow(() -> authDao.getAuth(loginResponse.authToken())));
        var correctToken = loginResponse.authToken();

        // log out with the incorrect token, expecting failure
        Assertions.assertDoesNotThrow(() -> service.logout("invalidAuthToken"));

        // check that the correct AuthToken is still in the DAO
        Assertions.assertNotNull(Assertions.assertDoesNotThrow(() -> authDao.getAuth(correctToken)));
    }

    @Test
    @DisplayName("Successful authorize user")
    void authorizeUser(){
        Assertions.assertDoesNotThrow(()-> userDao.registerUser("testUser", "testPassword", "test@test.com"));


        AuthData testToken = new AuthData(testUser.getUsername());
        Assertions.assertDoesNotThrow(() -> authDao.addAuth(testToken));

        Assertions.assertDoesNotThrow(() -> service.authoriseUser(testToken.getAuthToken()));
    }

    @Test
    @DisplayName("authorize user fail")
    void authorizeUserFail(){
        Assertions.assertThrows(DataAccessException.class, ()-> service.authoriseUser("invalidAuthToken"));
    }

}
