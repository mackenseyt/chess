package model;

import chess.ChessGame;


import java.util.Objects;

public class GameData {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final ChessGame game;
    private static int nextGameID = 1;

    public GameData(String gameName){
        this.gameID = nextGameID++;
        this.gameName = gameName;
        this.game = new ChessGame();
    }
    public GameData(int gameid, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        this.gameID = gameid;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = new ChessGame();
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public void setBlackUsername(String blackUsername){
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "gameID=" + gameID +
                ", whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'' +
                ", gameName='" + gameName + '\'' +
                ", game=" + game +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        return gameID == gameData.gameID && Objects.equals(whiteUsername, gameData.whiteUsername) && Objects.equals(blackUsername, gameData.blackUsername) && Objects.equals(gameName, gameData.gameName) && Objects.equals(game, gameData.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }


}
