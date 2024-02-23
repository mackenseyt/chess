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


public class GameServiceTest {

    static final GameDao gameDao = new GameDao();
    static final UserDao userDao = new UserDao();
    static final AuthDao authTokenDao = new AuthDao();
    static final UserData testUser = new UserData("testUser", "testPassword", "test@test.com");
    static final AuthData testAuth = new AuthData(testUser.getUsername());
    static final GameService service = new GameService();


    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
        Assertions.assertDoesNotThrow(userDao::clear);
        Assertions.assertDoesNotThrow(authTokenDao::clear);

        Assertions.assertTrue(userDao.isClear());
        Assertions.assertTrue(authTokenDao.isClear());
        Assertions.assertTrue(gameDao.isClear());

        Assertions.assertDoesNotThrow(()-> userDao.createUser(testUser));
        Assertions.assertDoesNotThrow(()-> authTokenDao.addAuth(testAuth));
    }

    @Test
    @DisplayName("CreateGameSuccess")
    public void createGame(){

    }
    @Test
    @DisplayName("CreateGame Fail")
    public void createGameFail(){

    }

    @Test
    @DisplayName("Join game")
    public void joinGame(){
        var testGame = new GameData("testGame");
        Assertions.assertDoesNotThrow(()-> gameDao.createGame(testGame));

        //join the game
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, testGame.getGameID(), testAuth.getAuthToken());
        Assertions.assertDoesNotThrow(()-> service.joinGame(joinGameRequest));

        //check the color and that the other color is available
        Assertions.assertEquals(testUser.getUsername(), Assertions.assertDoesNotThrow(()->gameDao.getGame(testGame.getGameID()).getBlackUsername()));
        Assertions.assertNull(Assertions.assertDoesNotThrow(()->gameDao.getGame(testGame.getGameID()).getWhiteUsername()));
    }
    @Test
    @DisplayName("Join game fail")
    public void joinGameFail(){

    }

    @Test
    @DisplayName("List game Success")
    public void listGame(){

    }
    @Test
    @DisplayName("List Games Fail")
    public void listGameFail(){

    }

}

