package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

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
        return switch(type){
            case PAWN -> movePawn(board, myPosition);
            case KING -> moveOne(board, myPosition,new int[][]{{1,0},{0,-1},{-1,0},{0,1}, {1,1},{1,-1},{-1,1},{-1,-1}});
            case KNIGHT -> moveOne(board, myPosition, new int[][]{{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}});
            case QUEEN -> moveLinear(board, myPosition,new int[][]{{1,0},{0,-1},{-1,0},{0,1}, {1,1},{1,-1},{-1,1},{-1,-1}});
            case ROOK -> moveLinear(board, myPosition, new int[][]{{1,0},{0,-1},{-1,0},{0,1}});
            case BISHOP -> moveLinear(board, myPosition, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
        };
    }

    private boolean isValidMove(ChessBoard board, ChessPosition start, ChessPosition end){
        int row = end.getRow();
        int col = end.getColumn();
        if(!(row>=1 && row <=8 && col>=1 && col<=8)){
            return false;
        }
        return board.getPiece(end) == null || board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor();
    }
    private void keepGoing(ChessBoard board, ChessPosition myPosition, int incRow, int incCol, Set<ChessMove> validMoves){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int targetRow = row+incRow;
        int targetCol = col +incCol;

        while (isValidMove(board, myPosition, new ChessPosition(targetRow, targetCol))){
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece targetPiece = board.getPiece(targetPosition);
            if(targetPiece == null){
                validMoves.add(new ChessMove(myPosition, targetPosition));
            }else{
                if(targetPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, targetPosition));
                }
                break;
            }
            targetRow += incRow;
            targetCol += incCol;
        }
    }

    private Collection<ChessMove> moveLinear(ChessBoard board, ChessPosition myPosition, int[][] moves){
        Set<ChessMove> validMoves = new HashSet<>();
        for(int[] move: moves){
            keepGoing(board, myPosition, move[0], move[1], validMoves);
        }
        return validMoves;
    }

    private Collection<ChessMove> moveOne(ChessBoard board, ChessPosition myPosition, int[][] moves){
        Set<ChessMove> validMoves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for(int[] move:moves){
            int targetRow = row+move[0];
            int targetCol = col + move[1];
            if(isValidMove(board, myPosition, new ChessPosition(targetRow, targetCol))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(targetRow, targetCol)));
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        ChessGame.TeamColor team = board.getPiece(myPosition).getTeamColor();
        int firstMove = (team == ChessGame.TeamColor.WHITE)? 2:7;
        int promotionRow = (team == ChessGame.TeamColor.WHITE)? 8:1;
        int direction = (team == ChessGame.TeamColor.WHITE)? 1:-1;

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        ChessPosition oneStep = new ChessPosition(row + direction, col);
        ChessPosition twoStep = new ChessPosition(row + 2*direction, col);
        ChessPosition captureLeft = new ChessPosition(row +direction, col-1);
        ChessPosition captureRight = new ChessPosition(row +direction, col +1);

        if(isValidMove(board, myPosition, oneStep) && board.getPiece(oneStep) == null && row +direction != promotionRow){
            validMoves.add(new ChessMove(myPosition, oneStep));
            if(isValidMove(board, myPosition,twoStep) && board.getPiece(twoStep) == null && row == firstMove){
                validMoves.add(new ChessMove(myPosition, twoStep));
            }
        }
        for(ChessPosition position: new ChessPosition[]{captureLeft, captureRight}){
            if(isValidMove(board, myPosition, position)&& board.getPiece(position) != null && board.getPiece(position).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                if(row+direction == promotionRow){
                    validMoves.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, position,PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                }else{
                    validMoves.add(new ChessMove(myPosition, position));
                }
            }
        }
        if(row+direction == promotionRow){
            validMoves.add(new ChessMove(myPosition,oneStep, PieceType.KNIGHT));
            validMoves.add(new ChessMove(myPosition, oneStep, PieceType.QUEEN));
            validMoves.add(new ChessMove(myPosition, oneStep,PieceType.ROOK));
            validMoves.add(new ChessMove(myPosition, oneStep, PieceType.BISHOP));
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
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
