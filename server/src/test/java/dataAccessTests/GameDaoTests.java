package dataAccessTests;

import chess.ChessGame;
import dataAccess.sqlDao.GameSqlDao;
import dataAccess.sqlDao.UserSqlDao;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDaoTests {
    private static final GameSqlDao gameDao;
    private static final UserSqlDao userDao;
    static {
        try {
            gameDao = new GameSqlDao();
            userDao = new UserSqlDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        Assertions.assertDoesNotThrow(gameDao::clear);
    }


    @Test
    void addGame(){
        GameData testGame = new GameData(1, null, null, "testGameName", new ChessGame());
        Assertions.assertDoesNotThrow(()-> gameDao.addGame(testGame));

        GameData foundGame = Assertions.assertDoesNotThrow(()-> gameDao.getGame(1));
        Assertions.assertEquals(testGame, foundGame);
    }

    @Test
    void getGame(){
        GameData testGame = new GameData(1, null, null, "testGameName", new ChessGame());
        Assertions.assertDoesNotThrow(()-> gameDao.addGame(testGame));

        GameData foundGame = Assertions.assertDoesNotThrow(()-> gameDao.getGame(1));

        Assertions.assertNotNull(foundGame);
        Assertions.assertEquals(testGame.getGameID(), foundGame.getGameID());
        Assertions.assertEquals(testGame.getGameName(), foundGame.getGameName());
        Assertions.assertEquals(testGame.getGame(), foundGame.getGame());
    }

    @Test
    void listGame(){
        GameData game1 = new GameData("testGame1");
        GameData game2 = new GameData("testGame2");

        Assertions.assertDoesNotThrow(() -> gameDao.addGame(game1));
        Assertions.assertDoesNotThrow(() -> gameDao.addGame(game2));

        ArrayList<GameData> games;

        try {
            games = gameDao.listGames();
        } catch (Exception e) {
            games = null;
        }

        Assertions.assertNotNull(games);
        Assertions.assertEquals(2, games.size());

        // Check if the test games are in the list
        Assertions.assertTrue(games.contains(game1));
        Assertions.assertTrue(games.contains(game2));
    }

    @Test
    void claimGame(){
        GameData testGame = new GameData("testGame");
        Assertions.assertDoesNotThrow(() -> gameDao.addGame(testGame));

        // add a user to the userDao to satisfy foreign key constraints, claim spot
        UserData testUser = new UserData("User", "password", "email");
        Assertions.assertDoesNotThrow(() -> userDao.registerUser("User", "password", "email"));
        Assertions.assertDoesNotThrow(() -> gameDao.claimGame(testUser.getUsername(), ChessGame.TeamColor.WHITE, testGame.getGameID()));

        // assert that the spot has been claimed
        var foundUsername = Assertions.assertDoesNotThrow(() -> gameDao.getGame(testGame.getGameID()).getWhiteUsername());
        Assertions.assertEquals(foundUsername, testUser.getUsername());
    }
}
