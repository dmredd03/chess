package chess;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
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
    private Boolean gameFinished = false;
    public ChessGame() {
        this.gameBoard = new ChessBoard();
        gameBoard.resetBoard();
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

        if (gameBoard.getPiece(startPosition) == null) { return null; }

        Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> badMoves = new ArrayList<>();

        // For each valid Move, test each possible board outcome
        for (ChessMove move : possibleMoves) {
            ChessGame possibleGameState = copy();

            try {
                possibleGameState.makeMove(move);
            } catch (InvalidMoveException e) {
                try {
                    if (possibleGameState.teamTurn == TeamColor.WHITE) {
                        possibleGameState.setTeamTurn(TeamColor.BLACK);
                    } else {
                        possibleGameState.setTeamTurn(TeamColor.WHITE);
                    }
                    possibleGameState.makeMove(move);
                } catch (InvalidMoveException f) {
                    badMoves.add(move);
                }
            }

        }
        possibleMoves.removeAll(badMoves);
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
        // if piece doesn't exist, throw exception
        if (pieceToMove == null) { throw new InvalidMoveException(); }
        // getting possible moves from chessPiece
        Collection<ChessMove> possibleValidMoves = pieceToMove.pieceMoves(gameBoard, move.getStartPosition());
        // if not a possible move in any way, throw exception
        boolean invalid = true;
        for (ChessMove possibleMoves : possibleValidMoves) {
            if (possibleMoves.equals(move)) {
                invalid = false;
                break;
            }
        }
        if (invalid) { throw new InvalidMoveException(); }
        // if not your turn, you can't move
        if (pieceToMove.getTeamColor() != teamTurn) { throw new InvalidMoveException(); }

        gameBoard.addPiece(move.getStartPosition(), null);
        // pawn promotion check
        if (pieceToMove.getPieceType() == ChessPiece.PieceType.PAWN) {
            // if pawn, change to promotion piece if applicable, else add normally
            if (pieceToMove.pieceColor == TeamColor.BLACK && move.getEndPosition().getRow() == 1) {
                gameBoard.addPiece(move.getEndPosition(), new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece()));
            } else if (pieceToMove.pieceColor == TeamColor.WHITE && move.getEndPosition().getRow() == 8) {
                gameBoard.addPiece(move.getEndPosition(), new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece()));
            } else {
                gameBoard.addPiece(move.getEndPosition(), pieceToMove);
            }
        } else {
            gameBoard.addPiece(move.getEndPosition(), pieceToMove);
        }

        // after movement checks
        if (isInCheck(pieceToMove.pieceColor)) { throw new InvalidMoveException(); }
        // stalemate check here
        if (pieceToMove.getTeamColor() == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean check = false;
        ChessPosition kingPos = getKingPosition(teamColor);
        // ChessPiece king = gameBoard.getPiece(kingPos);
        // Collection<ChessMove> kingMoves = king.pieceMoves(gameBoard, kingPos);

        if (inDanger(kingPos, teamColor)) { check = true; }
        return check;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) { return false; }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = gameBoard.getPiece(testPos);

                if (testPiece == null || testPiece.getTeamColor() != teamColor) { continue; }

                if (hasEscapeMove(testPiece, testPos, teamColor)) { return false; }
            }
        }

        return true;
    }

    private boolean hasEscapeMove(ChessPiece testPiece, ChessPosition testPos, TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = testPiece.pieceMoves(gameBoard, testPos);
        for (ChessMove move : possibleMoves) {
            ChessGame currentGameState = copy();

            try {
                currentGameState.makeMove(move);
                if (!currentGameState.isInCheck(teamColor)) { return true; }

            } catch (InvalidMoveException e){
                ;
            }
            // if king is out of check, not in checkmate
        }
        return false;
    }

    public ChessGame copy() {
        ChessGame copy = new ChessGame();
        copy.getBoard().clearBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece testPiece = gameBoard.getPiece(new ChessPosition(i, j));
                if (testPiece != null) {
                    ChessPiece copyPiece = new ChessPiece(testPiece);
                    copy.gameBoard.addPiece(new ChessPosition(i, j), copyPiece);
                }
            }
        }
        copy.teamTurn = this.teamTurn;
        return copy;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPos = getKingPosition(teamColor);

        // test if king is movable
        if (!validMoves(kingPos).isEmpty()) {
            return false;
        }
        // test if king is in check
        if (isInCheck(teamColor)) {
            return false;
        }
        // test if there are any validMoves available, if not then stalemate is true
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = gameBoard.getPiece(testPos);
                // if friendly piece, check for valid moves. If they exist, no stalemate
                if (testPiece != null && testPiece.getTeamColor() == teamColor) {
                    if (!validMoves(testPos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        // all tests fail, then it is stalemate
        return true;
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
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = gameBoard.getPiece(testPos);
                if (testPiece != null && testPiece.getPieceType() == ChessPiece.PieceType.KING && testPiece.getTeamColor() == team) {
                    return testPos;
                }
            }
        }
        return null;
    }

    private boolean inDanger(ChessPosition testPosition, TeamColor testTeamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition possiblePosition = new ChessPosition(i, j);
                ChessPiece possiblePiece = gameBoard.getPiece(possiblePosition);

                if (possiblePiece == null || possiblePiece.getTeamColor() == testTeamColor) { continue; }

                Collection<ChessMove> possibleMoves = getThreatMoves(possiblePiece, possiblePosition, testTeamColor);

                for (ChessMove move : possibleMoves) {
                    if (move.getEndPosition().equals(testPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Collection<ChessMove> getThreatMoves(ChessPiece testPiece, ChessPosition testPos, TeamColor targetTeamColor) {
        Collection<ChessMove> moves = new ArrayList<>(testPiece.pieceMoves(gameBoard, testPos));
        TeamColor enemyColor = (targetTeamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        moves.addAll(pawnAttackPrediction(enemyColor));
        return moves;
    }

    private Collection<ChessMove> pawnAttackPrediction(TeamColor testTeamColor) {
        Collection<ChessMove> pawnAttacks = new ArrayList<>();
        int direction = (testTeamColor == TeamColor.WHITE) ? 1 : -1;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPawn = gameBoard.getPiece(testPos);

                if (testPawn == null || testPawn.getTeamColor() != testTeamColor || testPawn.getPieceType() != ChessPiece.PieceType.PAWN) {
                    continue;
                }

                if (j > 1) {
                    addAttackMoveIfInBounds(pawnAttacks, testPos, direction, -1);
                }
                if (j < 8) {
                    addAttackMoveIfInBounds(pawnAttacks, testPos, direction, 1);
                }
            }
        }

        return pawnAttacks;
    }

    private void addAttackMoveIfInBounds(Collection<ChessMove> attacks, ChessPosition from, int rowDir, int colDir) {
        int newRow = from.getRow() + rowDir;
        int newCol = from.getColumn() + colDir;

        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            attacks.add(new ChessMove(from, new ChessPosition(newRow, newCol), null));
        }
    }

    public void setGameFinished(Boolean gameState) {
        gameFinished = gameState;
    }
    public Boolean getGameFinished() {
        return gameFinished;
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


}
