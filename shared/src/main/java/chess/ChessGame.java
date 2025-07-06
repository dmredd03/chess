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
        if (pieceToMove == null) throw new InvalidMoveException();
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
        if (invalid) throw new InvalidMoveException();
        // if not your turn, you can't move
        if (pieceToMove.getTeamColor() != teamTurn) throw new InvalidMoveException();

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
        if (isInCheck(pieceToMove.pieceColor)) throw new InvalidMoveException();
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

        if (inDanger(kingPos, teamColor)) check = true;
        return check;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPos = getKingPosition(teamColor);
        ChessPiece king = gameBoard.getPiece(kingPos);
        Collection<ChessMove> kingMoves = king.pieceMoves(gameBoard, kingPos);

        // if not in check, not in checkmate
        if (!isInCheck(teamColor)) return false;

        // check if any movement taken could get king out of check
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPiece = gameBoard.getPiece(testPos);
                // if a piece is on my team, test where I can move it and if that move gets the king to safety
                if (testPiece != null && testPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> possibleMoves = testPiece.pieceMoves(gameBoard, testPos);
                    for (ChessMove move : possibleMoves) {
                        ChessGame currentGameState = copy();

                        try {
                            currentGameState.makeMove(move);
                            if (isInCheck(teamColor)) return false;

                        } catch (InvalidMoveException e){
                            ;
                        }
                        // if king is out of check, not in checkmate

                    }
                }
            }
        }

        // check if moving the king gets him out of check
        /*for (ChessMove possibleKingMove : kingMoves) {
            // if (!inDanger(possibleKingMove.getEndPosition(), teamColor)) return false;

            ChessGame kingScenario = new ChessGame();
            kingScenario.setBoard(gameBoard);
            kingScenario.setTeamTurn(teamColor);
            kingScenario.getBoard().addPiece(kingPos, null);
            kingScenario.getBoard().addPiece(possibleKingMove.getEndPosition(), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
            if (!kingScenario.inDanger(possibleKingMove.getEndPosition(), teamColor)) return false;
        }*/

        return true;
    }

    public ChessGame copy() {
        ChessGame copy = new ChessGame();
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
        // TODO: Come back to implement isInStalemate after validMove is implemented
        ChessPosition kingPos = getKingPosition(teamColor);
        ChessPiece king = gameBoard.getPiece(kingPos);
        Collection<ChessMove> kingMoves = king.pieceMoves(gameBoard, kingPos);

        // test if king is movable
        for (ChessMove possibleKingMove : kingMoves) {
            if (inDanger(possibleKingMove.getEndPosition(), teamColor)) {
                continue;
            } else {
                return false;
            }
        }

        if (inDanger(kingPos, teamColor)) {
            return false;
        } else {
            return true;
        }
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
        boolean isInDanger = false;
        // check every position for validMoves, if they have endPosition at testPosition, isInDanger
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition possiblePosition = new ChessPosition(i, j);
                ChessPiece possiblePiece = gameBoard.getPiece(possiblePosition);
                if (possiblePiece != null && possiblePiece.getTeamColor() != testTeamColor) {
                    // adds all possible moves
                    Collection<ChessMove> possibleMoves = possiblePiece.pieceMoves(gameBoard, possiblePosition);
                    // adds pawn attacks
                    if (testTeamColor == TeamColor.WHITE) {
                        possibleMoves.addAll(pawnAttackPrediction(TeamColor.BLACK));
                    } else {
                        possibleMoves.addAll(pawnAttackPrediction(TeamColor.WHITE));
                    }

                    for (ChessMove testMove : possibleMoves) {
                        if (testMove.getEndPosition().equals(testPosition)) {
                            isInDanger = true;
                        }
                    }
                }
            }
        }
        return isInDanger;
    }

    private Collection<ChessMove> pawnAttackPrediction(TeamColor testTeamColor) {
        Collection<ChessMove> pawnAttacks = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPos = new ChessPosition(i, j);
                ChessPiece testPawn = gameBoard.getPiece(testPos);
                if (testPawn != null && testPawn.getTeamColor() == testTeamColor && testPawn.getPieceType() == ChessPiece.PieceType.PAWN) {
                    // Adds attack checks, doesn't worry about promotion or anything else, used to calculate danger for other pieces
                    if (testPos.getColumn() > 1) {
                        if (testTeamColor == TeamColor.WHITE) {
                            ChessPosition leftAttackPos = new ChessPosition(testPos.getRow() + 1, testPos.getColumn() - 1);
                            ChessMove leftAttack = new ChessMove(testPos, leftAttackPos, null);
                            pawnAttacks.add(leftAttack);
                        } else {
                            ChessPosition leftAttackPos = new ChessPosition(testPos.getRow() - 1, testPos.getColumn() - 1);
                            ChessMove leftAttack = new ChessMove(testPos, leftAttackPos, null);
                            pawnAttacks.add(leftAttack);
                        }
                    }

                    if (testPos.getColumn() < 8) {
                        if (testTeamColor == TeamColor.WHITE) {
                            ChessPosition rightAttackPos = new ChessPosition(testPos.getRow() + 1, testPos.getColumn() + 1);
                            ChessMove rightAttack = new ChessMove(testPos, rightAttackPos, null);
                            pawnAttacks.add(rightAttack);
                        } else {
                            ChessPosition rightAttackPos = new ChessPosition(testPos.getRow() - 1, testPos.getColumn() + 1);
                            ChessMove rightAttack = new ChessMove(testPos, rightAttackPos, null);
                            pawnAttacks.add(rightAttack);
                        }
                    }
                }
            }
        }

        return pawnAttacks;
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
