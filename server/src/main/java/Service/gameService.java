package Service;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class gameService {


    public static int createGame(String authToken, String gameName) {
        // Logic to create a new game

//        check auth token

//        initialize game
        int gameID = GameDAO.createGame();
        // Return the gameID of the newly created game
        return gameID;
    }

    public void joinGame(String authToken, int gameID, String playerColor) {
        // Logic to join a game
        throw new RuntimeException("not written");
    }
    public Collection<GameData> listGames(String authToken) {
        // Logic to retrieve a list of all games
        // Return a list of GameData objects
        throw new RuntimeException("not written");
    }

}
