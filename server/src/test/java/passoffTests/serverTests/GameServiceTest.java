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
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGameResponse;
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

        Assertions.assertDoesNotThrow(()-> userDao.addUser(testUser));
        Assertions.assertDoesNotThrow(()-> authTokenDao.addAuth(testAuth));
    }

    @Test
    @DisplayName("CreateGameSuccess")
    public void createGame(){
        CreateGameRequest createGameRequest = new CreateGameRequest("testGame");
        CreateGameResponse createGameResponse = Assertions.assertDoesNotThrow(()->service.createGame(createGameRequest));

        GameData testGame = Assertions.assertDoesNotThrow(()-> gameDao.getGame(createGameResponse.gameID()));
        Assertions.assertNotNull(testGame);
        Assertions.assertEquals(createGameRequest.gameName(), testGame.getGameName());
    }

    @Test
    @DisplayName("CreateGame Fail")
    public void createGameFail(){
        var testGame = new GameData("testGame");
        testGame.setGameID(testGame.getGameID() + 1);
        Assertions.assertDoesNotThrow(()-> gameDao.addGame(testGame));

        CreateGameRequest createGameRequest = new CreateGameRequest("testGame");
        Assertions.assertThrows(DataAccessException.class, ()-> service.createGame(createGameRequest));

    }

    @Test
    @DisplayName("Join game")
    public void joinGame(){
        var testGame = new GameData("testGame");
        Assertions.assertDoesNotThrow(()-> gameDao.addGame(testGame));

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
        GameData testGame = new GameData("testGame");
        Assertions.assertDoesNotThrow(()-> gameDao.addGame(testGame));

        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, 0, testAuth.getAuthToken());
        Assertions.assertThrows(DataAccessException.class, ()-> service.joinGame(joinGameRequest));
    }

    @Test
    @DisplayName("List game Success")
    public void listGame(){
        GameData testGame1 = new GameData("testGame1");
        GameData testGame2 = new GameData("testGame2");

        Assertions.assertDoesNotThrow(()-> gameDao.addGame(testGame1));
        Assertions.assertDoesNotThrow(()-> gameDao.addGame(testGame2));

        ListGameResponse listGameResponse = Assertions.assertDoesNotThrow(service::listGames);
        Assertions.assertEquals(2, listGameResponse.games().size());
        Assertions.assertTrue(listGameResponse.games().contains(testGame1));
        Assertions.assertTrue(listGameResponse.games().contains(testGame2));

    }


}

