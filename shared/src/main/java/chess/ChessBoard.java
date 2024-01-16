package chess;


import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
//\        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        var row = position.getRow()-1;
        var col = position.getColumn()-1;
        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        var row = position.getRow()-1;
        var col = position.getColumn()-1;
        return board[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        initializeRow(0, ChessGame.TeamColor.WHITE, false);
        initializeRow(1, ChessGame.TeamColor.WHITE, true);
        initializeRow(6, ChessGame.TeamColor.BLACK, true);
        initializeRow(7, ChessGame.TeamColor.BLACK, false);
    }

    private void initializeRow(int row, ChessGame.TeamColor teamColor, boolean pawn) {
        if (pawn){
            for (int col = 0; col < 8; col++) {
                board[row][col] = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            }
        }
        else{
            board[row][0] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            board[row][1] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            board[row][2] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            board[row][3] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
            board[row][4] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
            board[row][5] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            board[row][6] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            board[row][7] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(ChessPiece[] row : board){
            sb.append(Arrays.toString(row)).append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}

