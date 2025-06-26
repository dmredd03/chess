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
        }

        return possibleMoves;
    }

    private void BishopMovesCalculator() {
        int currRow = startPosition.getRow();
        int currCol = startPosition.getColumn();

        // up-right direction checks
        currRow++;
        currCol++;
        while (currRow < 8 && currCol < 8) {

            ChessPosition currChessMovePosition = new ChessPosition(currRow, currCol);
            ChessPiece chessPiece = board.getPiece(currChessMovePosition);
            // if empty
            if (chessPiece == null) {
                ChessMove possibleMove = new ChessMove(startPosition, currChessMovePosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(possibleMove);
                // if enemy
            } else if (chessPiece.getTeamColor() != piece.getTeamColor()) {

                ChessMove possibleMove = new ChessMove(startPosition, currChessMovePosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(possibleMove);
                break;
                // if friendly piece
            } else if (chessPiece.getTeamColor() == piece.getTeamColor()) {

                break;
            }
            currRow++;
            currCol++;
        }

    }


}
