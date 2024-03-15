package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class GamePlayUi {
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private static final int BOARD_SIZE = 8;
    private final String authToken;
    private ChessGame game;
    private ChessGame.TeamColor teamColor;
    List<ChessPosition> allChessPositions = new ArrayList<>();

    
    public GamePlayUi(ChessGame game, ChessGame.TeamColor teamColor, String authToken){
        this.authToken = authToken;
        out.println("We will start in just one moment");
        out.println();
        this.game = game;
        this.teamColor = teamColor;
        for (int i = 1; i < BOARD_SIZE + 1; i++){
            for (int j = 1; j < BOARD_SIZE + 1; j++){
                ChessPosition position = new ChessPosition(i, j);
                allChessPositions.add(position);
            }
        }
        printBlackBoard(game.getBoard());
        printWhiteBoard(game.getBoard());
//        out.println(game);
        end();
    }

    private void printBlackBoard(ChessBoard board) {
        int position = 0;
        int backIdx = 0;
        blackHeaders();
        for(int i = 1; i < BOARD_SIZE+1; i--){
            rowLabel(i);
            for(int j = 1; j < BOARD_SIZE+1; j+=2){
                if(board.getPiece(allChessPositions.get(position)) == null){
                    printBlankTile(backIdx);
                }else{
                    ChessPiece piece = board.getPiece(allChessPositions.get(position));
                    printPieces(piece, backIdx);
                }
            }
        }
        blackHeaders();


    }
    private void rowLabel(int index){
        out.println(SET_BG_COLOR_DARK_GREY);
        out.println(SET_TEXT_COLOR_WHITE);
        out.println(SET_TEXT_BOLD);
        out.print(" ");
        out.print(index);
        out.print(" ");
        resetBG();
    }
    private void printWhiteBoard(ChessBoard board){
        int position = 63;
        int backIdx = 0;
        whiteHeaders();
        whiteHeaders();
    }
    private void blackHeaders(){
        out.println(SET_BG_COLOR_DARK_GREY);
        out.println(SET_TEXT_COLOR_WHITE);
        out.println(SET_TEXT_BOLD);
        String[] headers = {"   ", " h", "   g", "   f", "  e", "   d", "   c", "  b", "   a", "    "};
        for (int i = 0; i < BOARD_SIZE + 2; i++){
            out.print(headers[i]);
        }
        resetBG();
        out.println();

    }

    private void whiteHeaders(){
        out.println(SET_BG_COLOR_DARK_GREY);
        out.println(SET_TEXT_COLOR_WHITE);
        out.println(SET_TEXT_BOLD);
        String[] headers = {"   ", " a", "   b", "   c", "  d", "   e", "   f", "  g", "   h", "    "};
        for (int i = 0; i < BOARD_SIZE + 2; i++){
            out.print(headers[i]);
        }
        resetBG();
        out.println();
    }
    private void printPieces(ChessPiece piece, int indx){
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){out.print(WHITE_KING);}
            else{out.print(BLACK_KING);}
        }else if(piece.getPieceType()== ChessPiece.PieceType.QUEEN){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){out.print(WHITE_QUEEN);}
            else{out.print(BLACK_QUEEN);}
        }
    }
    private void colorPicker(int bkg, ChessGame.TeamColor txt){
        if (bkg == 0) out.println(SET_BG_COLOR_WHITE);
        else out.println(SET_BG_COLOR_BLACK);

        if (txt == ChessGame.TeamColor.WHITE) out.println(SET_TEXT_COLOR_WHITE);
        else out.println(SET_TEXT_COLOR_BLACK);
    }
    private void printBlankTile(int bkg){
        if (bkg == 0) out.println(SET_BG_COLOR_WHITE);
        else out.println(SET_BG_COLOR_BLACK);
        out.print(EMPTY);
    }
    private void resetBG() {
        out.println(RESET_BG_COLOR);
    }
//    remove when further in project
    public void end(){
        new PostLoginUi(authToken);
    }

}
