package ui;

import chess.ChessGame;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class GamePlayUi {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private static final int BOARD_SIZE = 8;
    private String authToken;
    private GameData game;
    private ChessGame.TeamColor teamColor;
//    private int gameID;
    
    public GamePlayUi(GameData game, ChessGame.TeamColor teamColor, String authToken){
        this.authToken = authToken;
        out.println("We will start in just one moment");
        out.println();
        this.game = game;
        this.teamColor = teamColor;
        printWhiteBoard();
        printBlackBoard();
//        out.println(game);
        end();
    }

    private void printBlackBoard() {

    }

    private void printWhiteBoard(){

    }
    public void end(){
        new PostLoginUi(authToken);
    }

}
