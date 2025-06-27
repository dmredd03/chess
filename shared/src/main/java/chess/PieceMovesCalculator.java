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
            BishopMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            KnightMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            RookMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            QueenMovesCalculator();
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            KingMovesCalculator();
        } else { // ChessPiece.PieceType.PAWN
            PawnMovesCalculator();
        }

        return possibleMoves;
    }

    private void BishopMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // up-right direction checks
        currRow++;
        currCol++;
        while (currRow <= 8 && currCol <= 8) {

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
            currRow++;
            currCol++;
        }

        // down right direction
        currRow = startPosition.getRow();
        currCol = startPosition.getColumn();

        currRow--;
        currCol++;
        while (currRow >= 1 && currCol <= 8) {

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
            currRow--;
            currCol++;
        }
        // down left
        currRow = startPosition.getRow();
        currCol = startPosition.getColumn();

        currRow--;
        currCol--;
        while (currRow >= 1 && currCol >= 1) {

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
            currRow--;
            currCol--;
        }
        // up left
        currRow = startPosition.getRow();
        currCol = startPosition.getColumn();

        currRow++;
        currCol--;
        while (currRow <= 8 && currCol >= 1) {

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
            currRow++;
            currCol--;
        }
    }

    //Start Rook movement code
    private void RookMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // up

        currRow++;
        while(currRow <= 8) {
            ChessPosition currChessPos = new ChessPosition(currRow, currCol);
            ChessPiece chessPiece = board.getPiece(currChessPos);
            // if empty
            if (chessPiece == null) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                // if enemy piece
            } else if (chessPiece.getTeamColor() != piece.getTeamColor()) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                break;
                // if same piece
            } else {
                break;
            }
            currRow++;
        }

        // down
        currRow = startPosition.getRow();
        currCol = startPosition.getColumn();

        currRow--;
        while(currRow >= 1) {
            ChessPosition currChessPos = new ChessPosition(currRow, currCol);
            ChessPiece chessPiece = board.getPiece(currChessPos);
            // if empty
            if (chessPiece == null) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                // if enemy piece
            } else if (chessPiece.getTeamColor() != piece.getTeamColor()) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                break;
                // if same piece
            } else {
                break;
            }
            currRow--;
        }

        // right

        currRow = startPosition.getRow();
        currCol = startPosition.getColumn();

        currCol++;
        while(currCol <= 8) {
            ChessPosition currChessPos = new ChessPosition(currRow, currCol);
            ChessPiece chessPiece = board.getPiece(currChessPos);
            // if empty
            if (chessPiece == null) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                // if enemy piece
            } else if (chessPiece.getTeamColor() != piece.getTeamColor()) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                break;
                // if same piece
            } else {
                break;
            }
            currCol++;
        }

        // left

        currRow = startPosition.getRow();
        currCol = startPosition.getColumn();

        currCol--;
        while(currCol >= 1) {
            ChessPosition currChessPos = new ChessPosition(currRow, currCol);
            ChessPiece chessPiece = board.getPiece(currChessPos);
            // if empty
            if (chessPiece == null) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                // if enemy piece
            } else if (chessPiece.getTeamColor() != piece.getTeamColor()) {
                ChessMove validMove = new ChessMove(startPosition, currChessPos, null);
                possibleMoves.add(validMove);
                break;
                // if same piece
            } else {
                break;
            }
            currCol--;
        }
    }

    // Start Queen movement code
    private void QueenMovesCalculator() {
        BishopMovesCalculator();
        RookMovesCalculator();
    }

    // Start Knight movement code
    private void KnightMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // test each direction

        KnightTestMovablePosition(currRow + 2, currCol + 1); // up 2 right 1
        KnightTestMovablePosition(currRow + 1, currCol + 2); // up 1 right 2
        KnightTestMovablePosition(currRow - 1, currCol + 2); // down 1 right 2
        KnightTestMovablePosition(currRow - 2, currCol + 1); // down 2 right 1
        KnightTestMovablePosition(currRow - 2, currCol - 1); // down 2 left 1
        KnightTestMovablePosition(currRow - 1, currCol - 2); // down 1 left 2
        KnightTestMovablePosition(currRow + 1,  currCol - 2); // up 1 left 2
        KnightTestMovablePosition(currRow + 2, currCol - 1); // up 2 left 1
    }

    private void KnightTestMovablePosition(int currRow, int currCol) {

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
            // Your own piece is here, do not add possible move
        }
    }

    // Start King movement code
    private void KingMovesCalculator() {
        int kingRow = startPosition.getRow();
        int kingCol = startPosition.getColumn();
        KingTestMovablePosition(kingRow + 1, kingCol); // up
        KingTestMovablePosition(kingRow + 1, kingCol + 1); // up right
        KingTestMovablePosition(kingRow, kingCol + 1); // right
        KingTestMovablePosition(kingRow - 1, kingCol + 1); // down right
        KingTestMovablePosition(kingRow - 1, kingCol); // down
        KingTestMovablePosition(kingRow - 1, kingCol - 1); // down left
        KingTestMovablePosition(kingRow, kingCol - 1); // left
        KingTestMovablePosition(kingRow + 1, kingCol - 1); // up left
    }

    private void KingTestMovablePosition(int currRow, int currCol) {
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
    private void PawnMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // movement

        // white
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition forwardPosition = new ChessPosition(currRow + 1, currCol);
            ChessPiece forwardPiece = board.getPiece(forwardPosition);
            if (forwardPiece == null) {
                PawnPromotionCheck(forwardPosition);
                if (startPosition.getRow() == 2) {
                    ChessPosition doubleForwardPosition = new ChessPosition(currRow + 2, currCol);
                    ChessPiece doubleForwardPiece = board.getPiece(doubleForwardPosition);
                    if (doubleForwardPiece == null) PawnPromotionCheck(doubleForwardPosition);
                }
            }
            // black
        } else {
            ChessPosition forwardPosition = new ChessPosition(currRow - 1, currCol);
            ChessPiece forwardPiece = board.getPiece(forwardPosition);
            if (forwardPiece == null) {
                PawnPromotionCheck(forwardPosition);
                if (startPosition.getRow() == 7) {
                    ChessPosition doubleForwardPosition = new ChessPosition(currRow - 2, currCol);
                    ChessPiece doubleForwardPiece = board.getPiece(doubleForwardPosition);
                    if (doubleForwardPiece == null) PawnPromotionCheck(doubleForwardPosition);
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
                    PawnPromotionCheck(leftAttackPos);
                }
            }

            if (currCol < 8) {
                ChessPosition rightAttackPos = new ChessPosition(currRow + 1, currCol + 1);
                ChessPiece rightAttackPiece = board.getPiece(rightAttackPos);

                if (rightAttackPiece == null) {
                    ; //continue
                } else if (rightAttackPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    PawnPromotionCheck(rightAttackPos);
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
                    PawnPromotionCheck(leftAttackPos);
                }
            }

            if (currCol < 8) {
                ChessPosition rightAttackPos = new ChessPosition(currRow - 1, currCol + 1);
                ChessPiece rightAttackPiece = board.getPiece(rightAttackPos);

                if (rightAttackPiece == null) {
                    ; // continue
                } else if (rightAttackPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    PawnPromotionCheck(rightAttackPos);
                }
            }
        }
    }

    private void PawnPromotionCheck(ChessPosition testPosition) {
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
