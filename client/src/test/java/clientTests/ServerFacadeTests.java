package clientTests;

import chess.ChessGame;
import client.ServerFacade;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import request.JoinGameRequest;
import request.RegisterRequest;
import response.LoginResponse;
import server.Server;

import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    public static String user1 = "user1";
    public static String user2 = "user2";
    private String authToken;



    @BeforeAll
    public static void init() {
        server = new Server();

        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:"+ port);
        System.out.println("Started test HTTP server on " + port);
    }
    @BeforeEach
    public void setup(){
        Assertions.assertDoesNotThrow(()-> serverFacade.clear());
        var response = Assertions.assertDoesNotThrow(()-> serverFacade.register(user1, user1, user1));
        this.authToken = response.authToken();

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @DisplayName("Successful clear")
    public void clear(){
        Assertions.assertDoesNotThrow(()-> serverFacade.clear());
    }
    @Test
    @DisplayName("Register Success")
    public void registerPositive() {
        Assertions.assertDoesNotThrow(()-> serverFacade.register(user2, user2, user2));
    }
    @Test
    @DisplayName("Register fail")
    public void registerNegative() {
        Assertions.assertDoesNotThrow(()-> serverFacade.register(user2, user2, user2));
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.register(user2, user2, user2));
    }
    @Test
    @DisplayName("Login Success")
    public void loginPositive() {
        Assertions.assertDoesNotThrow(()->serverFacade.login(user1, user1));
    }
    @Test
    @DisplayName("Login fail")
    public void loginNegative() {
        Assertions.assertThrows(ResponseException.class, ()->serverFacade.login(user1, user2));
    }
    @Test
    @DisplayName("Logout Success")
    public void logoutPositive() {
        LoginResponse response = Assertions.assertDoesNotThrow(()->serverFacade.login(user1, user1));
        Assertions.assertDoesNotThrow(()-> serverFacade.logout(response.authToken()));
    }
    @Test
    @DisplayName("Logout fail")
    public void logoutNegative() {
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.logout("invalid authToken"));
    }
    @Test
    @DisplayName("List Games Success")
    public void listGamesPositive() {
        Assertions.assertDoesNotThrow(()-> serverFacade.createGame("testGame", authToken));
        Assertions.assertDoesNotThrow(()-> serverFacade.createGame("testGame2", authToken));

        List<GameData> games = Assertions.assertDoesNotThrow(()->serverFacade.listGames(authToken).games());

        Assertions.assertNotNull(games);
        Assertions.assertEquals(2, games.size());
    }
    @Test
    @DisplayName("List Games fail")
    public void listGamesNegative() {
        Assertions.assertDoesNotThrow(()-> serverFacade.createGame("testGame", authToken));
        Assertions.assertDoesNotThrow(()-> serverFacade.createGame("testGame2", authToken));

        Assertions.assertThrows(ResponseException.class, ()->serverFacade.listGames("invalid authToken").games());
    }
    @Test
    @DisplayName("Join game Success")
    public void joinGamePositive() {
        var response = Assertions.assertDoesNotThrow(()-> serverFacade.createGame("testGame", authToken));
        Assertions.assertDoesNotThrow(()-> serverFacade.joinGame(response.gameID(), ChessGame.TeamColor.BLACK, authToken));

    }
    @Test
    @DisplayName("Logout fail")
    public void joinGameNegative() {
        var response = Assertions.assertDoesNotThrow(()-> serverFacade.createGame("testGame", authToken));
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.joinGame(1, ChessGame.TeamColor.BLACK, authToken));

    }
    @Test
    @DisplayName("Create Game Success")
    public void createGamePositive() {
        var response = Assertions.assertDoesNotThrow(()-> serverFacade.createGame("testGame", authToken));
        Assertions.assertNotNull(response);
    }
    @Test
    @DisplayName("CreateGame fail")
    public void createGameNegative() {
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.createGame("testGame", "invalid authToken"));
    }


}