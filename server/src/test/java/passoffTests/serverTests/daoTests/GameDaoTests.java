package passoffTests.serverTests.daoTests;

import chess.ChessGame;
import dataAccess.sqlDao.GameSqlDao;
import dataAccess.sqlDao.UserSqlDao;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDaoTests {
    private static final GameSqlDao gameDao;
    static {
        try {
            gameDao = new GameSqlDao();
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
}
