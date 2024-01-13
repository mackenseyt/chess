package chess;

import java.util.Collection;

import java.util.Collections;


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
            case PAWN -> pawnMoves();
            case ROOK -> rookMoves();
            case KNIGHT -> knightMoves();
            case BISHOP -> bishopMoves(board, myPosition);
            case QUEEN -> queenMoves();
            case KING -> kingMoves();
        };
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

    private Collection<ChessMove> rookMoves() {
        return Collections.emptyList();
    }

    private Collection<ChessMove> pawnMoves() {
        return Collections.emptyList();
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        return Collections.emptyList();
    }
}
