package Service;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {


    public static int createGame(String authToken, String gameName) throws DataAccessException {
        // Logic to create a new game
//        check auth token
        AuthDAO.getAuth(authToken);
//        if(auth == null){
//            return 0;
//        }
//        initialize game
        int gameID = GameDAO.createGame(gameName);
        // Return the gameID of the newly created game
        return gameID;
    }

    public void joinGame(String authToken, int gameID, String playerColor) {
        // Logic to join a game
        throw new RuntimeException("not written");
    }
    public static Collection<GameData> listGames(String authToken) throws DataAccessException {
//       check authtoken

        // Return a list of GameData objects
        Collection<GameData> games = GameDAO.listGames();
        return games;
    }

}
