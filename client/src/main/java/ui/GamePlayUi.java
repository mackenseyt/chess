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
//        out.println("We will start in just one moment");
        out.println();
        this.game = game;
        this.teamColor = teamColor;
        for (int i = 1; i < BOARD_SIZE + 1; i++){
            for (int j = 1; j < BOARD_SIZE + 1; j++){
                ChessPosition position = new ChessPosition(i, j);
                allChessPositions.add(position);
            }
        }
        printWhiteBoard(game.getBoard());
        out.println();
        printBlackBoard(game.getBoard());
    }

    private void printBlackBoard(ChessBoard board) {
        int position = 0;
        int backIdx = 0;
        blackHeaders();
        for(int i = BOARD_SIZE; i > 0; i--){
            rowLabel(i);
            for(int j = 1; j < BOARD_SIZE+1; j++){
                if(board.getPiece(allChessPositions.get(position)) == null){
                    printBlankTile(backIdx);
                }else{
                    ChessPiece piece = board.getPiece(allChessPositions.get(position));
                    printPieces(piece, backIdx);
                }
                position++;
                if(backIdx == 1) backIdx = 0;
                else backIdx = 1;
            }
            rowLabel(i);
            resetBG();
            out.println();
            if (backIdx == 1) backIdx = 0;
            else backIdx = 1;
        }
        blackHeaders();


    }
    private void rowLabel(int index){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        out.print(" ");
        out.print(index);
        out.print(" ");
        resetBG();
    }
    private void printWhiteBoard(ChessBoard board){
        int position = 63;
        int backIdx = 0;
        whiteHeaders();
        for(int i = BOARD_SIZE; i > 0; i--){
            rowLabel(i);
            for(int j = 1; j < BOARD_SIZE+1; j++){
                if(board.getPiece(allChessPositions.get(position)) == null){
                    printBlankTile(backIdx);
                }else{
                    ChessPiece piece = board.getPiece(allChessPositions.get(position));
                    printPieces(piece, backIdx);
                }
                position--;
                if(backIdx == 1) backIdx = 0;
                else backIdx = 1;
            }
            rowLabel(i);
            resetBG();
            out.println();
            if (backIdx == 1) backIdx = 0;
            else backIdx = 1;
        }
        whiteHeaders();
    }
    private void blackHeaders(){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        String[] headers = {"  ", "  a", "  b", "  c", "  d", "  e", "  f", "  g", "  h", "    "};

        for (int i = 0; i < BOARD_SIZE + 2; i++){
            out.print(headers[i]);
        }
        resetBG();
        out.println();

    }

    private void whiteHeaders(){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        String[] headers = {"  ", "  h", "  g", "  f", "  e", "  d", "  c", "  b", "  a", "    "};
        for (int i = 0; i < BOARD_SIZE + 2; i++){
            out.print(headers[i]);
        }
        resetBG();
        out.println();
    }
    private void printPieces(ChessPiece piece, int indx){
        out.print(SET_TEXT_BOLD);
        if(piece.getPieceType() == ChessPiece.PieceType.KING){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(" K ");
            }else{
                out.print(SET_TEXT_COLOR_RED);
                out.print(" K ");
            }
        }else if(piece.getPieceType()== ChessPiece.PieceType.QUEEN){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(" Q ");}
            else{
                out.print(SET_TEXT_COLOR_RED);
                out.print(" Q ");}
        }else if(piece.getPieceType()== ChessPiece.PieceType.KNIGHT){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(" N ");}
            else{
                out.print(SET_TEXT_COLOR_RED);
                out.print(" N ");}
        }else if(piece.getPieceType()== ChessPiece.PieceType.BISHOP){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(" B ");}
            else{
                out.print(SET_TEXT_COLOR_RED);
                out.print(" B ");}
        }else if(piece.getPieceType()== ChessPiece.PieceType.ROOK){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(" R ");}
            else{
                out.print(SET_TEXT_COLOR_RED);
                out.print(" R ");}
        }else if(piece.getPieceType()== ChessPiece.PieceType.PAWN){
            colorPicker(indx, piece.getTeamColor());
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(" P ");}
            else{
                out.print(SET_TEXT_COLOR_RED);
                out.print(" P ");}
        }
    }
    private void colorPicker(int bkg, ChessGame.TeamColor txt){
        if (bkg == 0) out.print(SET_BG_COLOR_WHITE);
        else out.print(SET_BG_COLOR_BLACK);

        if (txt == ChessGame.TeamColor.WHITE) out.print(SET_TEXT_COLOR_WHITE);
        else out.print(SET_TEXT_COLOR_BLACK);
    }
    private void printBlankTile(int bkg){
        if (bkg == 0) out.print(SET_BG_COLOR_WHITE);
        else out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY);
    }
    private void resetBG() {
        out.print(RESET_BG_COLOR);
    }
}
