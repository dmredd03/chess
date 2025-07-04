package chess;

import javax.management.InvalidAttributeValueException;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard;
    private TeamColor teamTurn;
    public ChessGame() {
        this.gameBoard = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
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
        // TODO: implement checking and stalemates
        if (gameBoard.getPiece(startPosition) == null) return null;

        Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);

        // For each valid Move, test each possible board outcome
        for (ChessMove move : possibleMoves) {
            ChessGame possibleGameState = new ChessGame();
            possibleGameState.setBoard(gameBoard);
            possibleGameState.setTeamTurn(teamTurn);
            try {
                possibleGameState.makeMove(move);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
        }

        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = gameBoard.getPiece(move.getStartPosition());
        if (pieceToMove == null) throw new InvalidMoveException();
        gameBoard.addPiece(move.getStartPosition(), null);
        gameBoard.addPiece(move.getEndPosition(), pieceToMove);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // TODO: FIX
        boolean checkmate = false;
        ChessPosition kingPos = getKingPosition(teamColor);
        ChessPiece king = gameBoard.getPiece(kingPos);

        return checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return gameBoard;
    }

    private ChessPosition getKingPosition(TeamColor team) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; i <= 8; i++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = gameBoard.getPiece(testPos);
                if (testPiece != null && testPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return testPos;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "gameBoard=" + gameBoard +
                ", teamTurn=" + teamTurn +
                '}';
    }
}
