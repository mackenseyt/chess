package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

//    private final Set<ChessMove> validMoves = new HashSet<>();

    public ChessGame() {
//        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Set<ChessMove> pieceMoves = new HashSet<>(piece.pieceMoves(board, startPosition));
        removeInvalidMoves(pieceMoves);
        return pieceMoves;
    }

//    remove the move if it puts us in check
    private void removeInvalidMoves(Set<ChessMove> validMoves){
        validMoves.removeIf(moves -> {
            ChessPiece startPiece = board.getPiece(moves.getStartPosition());
            ChessPiece endPiece = board.getPiece(moves.getEndPosition());
            board.addPiece(moves.getEndPosition(), startPiece);
            board.addPiece(moves.getStartPosition(), null);
            boolean inCheck = isInCheck(startPiece.getTeamColor());
            board.addPiece(moves.getStartPosition(), startPiece);
            board.addPiece(moves.getEndPosition(), endPiece);

            return inCheck;
        });
    }

    /**
     * Find the king of the current team
     */
    private ChessPosition findKingPosition(TeamColor teamTurn){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition curPosition = new ChessPosition(i,j);
                ChessPiece curPiece = board.getPiece(curPosition);
                if(curPiece == null) continue;
                if(board.getPiece(curPosition).getTeamColor() == teamTurn &&
                        board.getPiece(curPosition).getPieceType().equals(ChessPiece.PieceType.KING)){
                    return curPosition;
                }
            }
        }
        return null;
    }
    
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
//        check team turn
        if(board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn){
            throw new InvalidMoveException("Wrong team");
        }
        Set<ChessMove> validMoves = new HashSet<>(validMoves(move.getStartPosition()));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition= findKingPosition(teamColor);
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition curPosition = new ChessPosition(i,j);
                ChessPiece curPiece = board.getPiece(curPosition);
                if(curPiece == null) continue;
                if(curPiece.getTeamColor() != teamColor){
                    Set<ChessMove> validMoves = new HashSet<>(curPiece.pieceMoves(board, curPosition));
                    for(var move: validMoves){
                        if (move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!canMove(teamColor)){
            return false;
        }
        return isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !canMove(teamColor);
    }

    private boolean canMove(TeamColor teamColor){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition curPosition = new ChessPosition(i,j);
                ChessPiece curPiece = board.getPiece(curPosition);
                if(curPiece == null) continue;
                if(curPiece.getTeamColor() == teamColor){
                    Set<ChessMove> validMoves = new HashSet<>(curPiece.pieceMoves(board, curPosition));
                    if(!validMoves.isEmpty()){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }
}
