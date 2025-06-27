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


}
