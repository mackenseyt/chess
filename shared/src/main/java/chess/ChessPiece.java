package chess;

import java.util.*;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return switch(type) {
            case PAWN -> pawnMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case BISHOP -> bishopMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            case KING -> kingMoves(board, myPosition);
        };
    }
    // check if it is a valid move
    private boolean isValidMove(ChessBoard board, ChessPosition start, ChessPosition end){
        //check if it is out of bounds
        if (!isValidPosition(end)){
            return false;
        }
        else{
            return board.getPiece(end) == null || board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor();
        }

    }
    //check if position is on the chess board
    private boolean isValidPosition(ChessPosition myPosition){
        return myPosition.getRow() >=1 && myPosition.getRow() <= 8 && myPosition.getColumn() >=1 && myPosition.getColumn() <=8;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        var teamColor = board.getPiece(myPosition).getTeamColor();

        int initialRow = (teamColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (teamColor == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int direction = (teamColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        ChessPosition firstDirections = new ChessPosition(currentRow + 2 * direction, currentCol);
        ChessPosition secondDirections = new ChessPosition(currentRow + direction, currentCol);
        ChessPosition captureRight = new ChessPosition(currentRow + direction, currentCol + 1);
        ChessPosition captureLeft = new ChessPosition(currentRow + direction, currentCol -1);

        if (isValidMove(board, myPosition, secondDirections) && board.getPiece(secondDirections)==null){
            validMoves.add(new ChessMove(myPosition, secondDirections, null));
            if (isValidMove(board, myPosition, firstDirections) && (board.getPiece(firstDirections) == null)&& currentRow == initialRow) {
                validMoves.add(new ChessMove(myPosition, firstDirections, null));
            }
        }
        for (ChessPosition capture: new ChessPosition[]{captureRight, captureLeft}){
            if (isValidMove(board, myPosition, capture)){
                ChessPiece target = board.getPiece(capture);
                if(target != null && target.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, capture, null));
                }
            }
            if (myPosition.getRow() == promotionRow) {
                for (var promotionPiece : new PieceType[]{PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                    ChessPosition promotionPosition = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
                    validMoves.add(new ChessMove(myPosition, promotionPosition, promotionPiece));
                }
            }
        }


        return validMoves;
    }
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        int[][] directions = {{1,0}, {-1,0}, {0, 1}, {0,-1},{-1,-1}, {-1,1}, {1,-1}, {1,1}};
        for (int[] direction: directions){
            int targetRow = currentRow + direction[0];
            int targetCol = currentCol + direction[1];
            ChessPosition finPosition = new ChessPosition(targetRow, targetCol);

            if (isValidPosition(finPosition) && (board.getPiece(finPosition)==null) || isValidPosition(finPosition) && (board.getPiece(finPosition).getTeamColor()!= board.getPiece(myPosition).getTeamColor())) {
                validMoves.add(new ChessMove(myPosition, finPosition, null));
            }

        }
        System.out.println("Valid Moves: " + validMoves);
        return validMoves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        int[][] directions = {{1,0}, {-1,0}, {0, 1}, {0,-1},{-1,-1}, {-1,1}, {1,-1}, {1,1}};
        for (int[] direction: directions){
            untilBlocked(board, myPosition, currentRow, currentCol, direction[0], direction[1], validMoves);

        }
        System.out.println("Valid Moves: " + validMoves);
        return validMoves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        int[][] directions = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
        for (int[] direction: directions){
            int targetRow = currentRow + direction[0];
            int targetCol = currentCol + direction[1];
            ChessPosition finPosition = new ChessPosition(targetRow, targetCol);

            if (isValidPosition(finPosition) && (board.getPiece(finPosition)==null) || isValidPosition(finPosition) && (board.getPiece(finPosition).getTeamColor()!= board.getPiece(myPosition).getTeamColor())) {
                validMoves.add(new ChessMove(myPosition, finPosition, null));
            }

        }
        System.out.println("Valid Moves: " + validMoves);
        return validMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        int[][] directions = {{1,0}, {-1,0}, {0, 1}, {0,-1}};
        for (int[] direction: directions){
            untilBlocked(board, myPosition, currentRow, currentCol, direction[0], direction[1], validMoves);

        }
        System.out.println("Valid Moves: " + validMoves);
        return validMoves;
    }


    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();
        int[][] directions = {{-1,-1}, {-1,1}, {1,-1}, {1,1}};
        for (int[] direction: directions){
            untilBlocked(board, myPosition, currentRow, currentCol, direction[0], direction[1], validMoves);

        }
//        System.out.println("Valid Moves: " + validMoves);
        return validMoves;

    }
    private void untilBlocked(ChessBoard board, ChessPosition myPosition,int currentRow, int currentCol, int rowIncrement, int colIncrement, Set<ChessMove> validMoves){

        int targetRow = currentRow + rowIncrement;
        int targetCol = currentCol + colIncrement;

        while (isValidMove(board, myPosition, new ChessPosition(targetRow, targetCol))){
            ChessPiece targetPiece = board.getPiece(new ChessPosition(targetRow, targetCol));
            if(targetPiece == null){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(targetRow, targetCol),null));
            }
            else{
                if (targetPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(targetRow, targetCol)));
                }
                break;
            }
            targetRow += rowIncrement;
            targetCol += colIncrement;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
