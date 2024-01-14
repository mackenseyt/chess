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
//        if (board.getPiece(myPosition) == null){
//            return new HashSet<>();
//        }
        return switch(type) {
            case PAWN -> pawnMoves();
            case ROOK -> rookMoves(board, myPosition);
            case KNIGHT -> knightMoves();
            case BISHOP -> bishopMoves(board, myPosition);
            case QUEEN -> queenMoves();
            case KING -> kingMoves();
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

    private Collection<ChessMove> pawnMoves() {
        return Collections.emptyList();
    }
    private Collection<ChessMove> kingMoves() {
        return Collections.emptyList();
    }

    private Collection<ChessMove> queenMoves() {
        return Collections.emptyList();
    }

    private Collection<ChessMove> knightMoves() {
        return Collections.emptyList();
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
        System.out.println("Valid Moves: " + validMoves);
        return validMoves;
//        List<ChessMove> sortedMoves = new ArrayList<>(validMoves);
//        Collections.sort(sortedMoves);
//
//        return sortedMoves;
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
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(targetRow, targetCol),targetPiece.getPieceType()));
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
