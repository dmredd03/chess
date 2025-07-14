package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    private ChessPosition startPosition;
    private Collection<ChessMove> possibleMoves;
    private ChessBoard board;
    private ChessPiece piece;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.startPosition = position;
        this.board = board;
        this.possibleMoves = new ArrayList<>();
    }

    public Collection<ChessMove> getPossibleMoves() {
        this.piece = board.getPiece(startPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            bishopMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            knightMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            rookMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            queenMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            kingMovesCalculator();
        } else { // ChessPiece.PieceType.PAWN
            pawnMovesCalculator();
        }

        return possibleMoves;
    }

    private void bishopMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // up-right direction checks
        movementCheckStraightDiagonal(1, 1);
        // down right direction
        movementCheckStraightDiagonal(-1, 1);
        // down left
        movementCheckStraightDiagonal(-1, -1);
        // up left
        movementCheckStraightDiagonal(1, -1);
    }

    private void movementCheckStraightDiagonal(int rowDir, int colDir) {
        int currRow = startPosition.getRow() + rowDir;
        int currCol = startPosition.getColumn() + colDir;

        while (currRow <= 8 && currCol >= 1 && currRow >= 1 && currCol <= 8) {
            ChessPosition currChessMovePosition = new ChessPosition(currRow, currCol);
            ChessPiece chessPiece = board.getPiece(currChessMovePosition);
            // if empty
            if (chessPiece == null) {
                ChessMove possibleMove = new ChessMove(startPosition, currChessMovePosition, null);
                possibleMoves.add(possibleMove);
                // if enemy
            } else if (chessPiece.getTeamColor() != piece.getTeamColor()) {
                ChessMove possibleMove = new ChessMove(startPosition, currChessMovePosition, null);
                possibleMoves.add(possibleMove);
                break;
                // if friendly piece
            } else if (chessPiece.getTeamColor() == piece.getTeamColor()) {
                break;
            }
            currRow += rowDir;
            currCol += colDir;
        }
    }

    //Start Rook movement code
    private void rookMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // up
        movementCheckStraightDiagonal(1, 0);

        // down
        movementCheckStraightDiagonal(-1, 0);

        // right
        movementCheckStraightDiagonal(0, 1);

        // left
        movementCheckStraightDiagonal(0, -1);
    }

    // Start Queen movement code
    private void queenMovesCalculator() {
        bishopMovesCalculator();
        rookMovesCalculator();
    }

    // Start Knight movement code
    private void knightMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // test each direction

        knightTestMovablePosition(currRow + 2, currCol + 1); // up 2 right 1
        knightTestMovablePosition(currRow + 1, currCol + 2); // up 1 right 2
        knightTestMovablePosition(currRow - 1, currCol + 2); // down 1 right 2
        knightTestMovablePosition(currRow - 2, currCol + 1); // down 2 right 1
        knightTestMovablePosition(currRow - 2, currCol - 1); // down 2 left 1
        knightTestMovablePosition(currRow - 1, currCol - 2); // down 1 left 2
        knightTestMovablePosition(currRow + 1,  currCol - 2); // up 1 left 2
        knightTestMovablePosition(currRow + 2, currCol - 1); // up 2 left 1
    }

    private void knightTestMovablePosition(int currRow, int currCol) {

        if (currCol > 8 || currCol < 1 || currRow > 8 || currRow < 1) return;

        ChessPosition currChessMovePosition = new ChessPosition(currRow, currCol);
        ChessPiece chessPiece = board.getPiece(currChessMovePosition);

        if (chessPiece == null) {
            ChessMove newMove = new ChessMove(startPosition, currChessMovePosition, null);
            possibleMoves.add(newMove);
        } else if (chessPiece.getTeamColor() != piece.getTeamColor()) {
            ChessMove newMove = new ChessMove(startPosition, currChessMovePosition, null);
            possibleMoves.add(newMove);
        } else {
            ; // Your own piece is here, do not add possible move
        }
    }

    // Start King movement code
    private void kingMovesCalculator() {
        int kingRow = startPosition.getRow();
        int kingCol = startPosition.getColumn();
        kingTestMovablePosition(kingRow + 1, kingCol); // up
        kingTestMovablePosition(kingRow + 1, kingCol + 1); // up right
        kingTestMovablePosition(kingRow, kingCol + 1); // right
        kingTestMovablePosition(kingRow - 1, kingCol + 1); // down right
        kingTestMovablePosition(kingRow - 1, kingCol); // down
        kingTestMovablePosition(kingRow - 1, kingCol - 1); // down left
        kingTestMovablePosition(kingRow, kingCol - 1); // left
        kingTestMovablePosition(kingRow + 1, kingCol - 1); // up left
    }

    private void kingTestMovablePosition(int currRow, int currCol) {
        if (currRow <= 8 && currRow >= 1 && currCol <= 8 && currCol >= 1) {
            ChessPosition testPos = new ChessPosition(currRow, currCol);
            ChessPiece currChessPiece = board.getPiece(testPos);
            // open space
            if (currChessPiece == null) {
                ChessMove validMove = new ChessMove(startPosition, testPos, null);
                possibleMoves.add(validMove);
                // enemy space
            } else if (currChessPiece.getTeamColor() != piece.getTeamColor()) {
                ChessMove validMove = new ChessMove(startPosition, testPos, null);
                possibleMoves.add(validMove);
                // friendly (not valid space)
            } else {
                return;
            }
        }
    }

    // Start Pawn movement code
    private void pawnMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // movement

        // white
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition forwardPosition = new ChessPosition(currRow + 1, currCol);
            ChessPiece forwardPiece = board.getPiece(forwardPosition);
            if (forwardPiece == null) {
                pawnPromotionCheck(forwardPosition);
                if (startPosition.getRow() == 2) {
                    ChessPosition doubleForwardPosition = new ChessPosition(currRow + 2, currCol);
                    ChessPiece doubleForwardPiece = board.getPiece(doubleForwardPosition);
                    if (doubleForwardPiece == null) pawnPromotionCheck(doubleForwardPosition);
                }
            }
            // black
        } else {
            ChessPosition forwardPosition = new ChessPosition(currRow - 1, currCol);
            ChessPiece forwardPiece = board.getPiece(forwardPosition);
            if (forwardPiece == null) {
                pawnPromotionCheck(forwardPosition);
                if (startPosition.getRow() == 7) {
                    ChessPosition doubleForwardPosition = new ChessPosition(currRow - 2, currCol);
                    ChessPiece doubleForwardPiece = board.getPiece(doubleForwardPosition);
                    if (doubleForwardPiece == null) pawnPromotionCheck(doubleForwardPosition);
                }
            }
        }

        // attacking movement
        // white
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && currRow < 8) {
            if (currCol > 1) {
                ChessPosition leftAttackPos = new ChessPosition(currRow + 1, currCol - 1);
                ChessPiece leftAttackPiece = board.getPiece(leftAttackPos);

                if (leftAttackPiece == null) {
                    ; // continue
                } else if (leftAttackPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    pawnPromotionCheck(leftAttackPos);
                }
            }

            if (currCol < 8) {
                ChessPosition rightAttackPos = new ChessPosition(currRow + 1, currCol + 1);
                ChessPiece rightAttackPiece = board.getPiece(rightAttackPos);

                if (rightAttackPiece == null) {
                    ; //continue
                } else if (rightAttackPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    pawnPromotionCheck(rightAttackPos);
                }
            }
        }
        // black
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (currCol > 1) {
                ChessPosition leftAttackPos = new ChessPosition(currRow - 1, currCol - 1);
                ChessPiece leftAttackPiece = board.getPiece(leftAttackPos);

                if (leftAttackPiece == null) {
                    ; // continue
                } else if (leftAttackPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    pawnPromotionCheck(leftAttackPos);
                }
            }

            if (currCol < 8) {
                ChessPosition rightAttackPos = new ChessPosition(currRow - 1, currCol + 1);
                ChessPiece rightAttackPiece = board.getPiece(rightAttackPos);

                if (rightAttackPiece == null) {
                    ; // continue
                } else if (rightAttackPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    pawnPromotionCheck(rightAttackPos);
                }
            }
        }
    }

    private void pawnPromotionCheck(ChessPosition testPosition) {
        ChessMove currentMove;

        if (testPosition.getRow() == 8 && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.QUEEN);
            possibleMoves.add(currentMove);
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.ROOK);
            possibleMoves.add(currentMove);
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.BISHOP);
            possibleMoves.add(currentMove);
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.KNIGHT);
            possibleMoves.add(currentMove);
        } else if (testPosition.getRow() == 1 && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.QUEEN);
            possibleMoves.add(currentMove);
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.ROOK);
            possibleMoves.add(currentMove);
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.BISHOP);
            possibleMoves.add(currentMove);
            currentMove = new ChessMove(startPosition, testPosition, ChessPiece.PieceType.KNIGHT);
            possibleMoves.add(currentMove);
        } else {
            currentMove = new ChessMove(startPosition, testPosition, null);
            possibleMoves.add(currentMove);
        }

    }


}
