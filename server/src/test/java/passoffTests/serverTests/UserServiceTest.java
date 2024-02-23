package passoffTests.serverTests;

import chess.ChessGame;
import dataAccess.AuthDao;
import dataAccess.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import request.JoinGameRequest;
import service.GameService;
import service.UserService;

public class UserServiceTest {
    static final GameDao gameDao = new GameDao();
    static final UserDao userDao = new UserDao();
    static final AuthDao authTokenDao = new AuthDao();
    static final UserService service = new UserService();
    static final UserData testUser = new UserData("testUser", "testPassword", "test@test.com");
    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authTokenDao::clear);

        Assertions.assertTrue(userDao.isClear());
        Assertions.assertTrue(authTokenDao.isClear());
        Assertions.assertTrue(gameDao.isClear());
    }

    @Test
    @DisplayName("Successful register")
    void register(){

    }
    @Test
    @DisplayName("register fail")
    void registerFail(){

    }
    @Test
    @DisplayName("Successful login")
    void login(){

    }
    @Test
    @DisplayName("login fail")
    void loginFail(){

    }
    @Test
    @DisplayName("Successful logout")
    void logout(){

    }
    @Test
    @DisplayName("logout fail")
    void logoutFail(){

    }
    @Test
    @DisplayName("Successful authorize user")
    void authorizeUser(){

    }
    @Test
    @DisplayName("authorize user fail")
    void authorizeUserFail(){

    }

}
