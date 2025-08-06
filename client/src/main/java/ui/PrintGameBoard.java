package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PrintGameBoard {
    private static String currBgColor;
    private static String currTextColor;
    private static final String HEADER_BG_COLOR = EscapeSequences.SET_BG_COLOR_RED;
    private static final String HEADER_TEXT_COLOR = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private ChessBoard board;
    private ChessGame.TeamColor perspective;
    private Boolean highlighting = false;
    private Collection<ChessPosition> highlightPosition = new ArrayList<>();

    public void printBoardWhite(ChessGame currGame) {
        board = currGame.getBoard();
        perspective = ChessGame.TeamColor.WHITE;

        printHeader();
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
        System.out.print(HEADER_BG_COLOR);
        System.out.print(" 8 ");
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        currBgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        for (int row = 8; row >= 1; row--) {
            currBgColor = (row % 2 == 0) ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_BLACK;

            for (int col = 1; col <= 8; col++) {
                ChessPosition currPos = new ChessPosition(row, col);
                if (highlighting && highlightPosition != null && highlightPosition.contains(currPos)) {
                    System.out.print(currBgColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                            ? EscapeSequences.SET_BG_COLOR_GREEN
                            : EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                } else {
                    System.out.print(currBgColor);
                }

                ChessPiece currPiece = board.getPiece(currPos);
                if (currPiece == null) {
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    printCurrSpace(currPiece, col, row);
                }

                currBgColor = currBgColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                        ? EscapeSequences.SET_BG_COLOR_BLACK
                        : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            }
            printVerticalNum(row, -1);
            System.out.print(currBgColor);
        }
        printHeader();
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
    }

    private void printCurrSpace(ChessPiece currPiece, int col, int row) {
        if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            currTextColor = EscapeSequences.SET_TEXT_COLOR_RED;
            System.out.print(currTextColor);
            switch (currPiece.getPieceType()) {
                case ChessPiece.PieceType.PAWN -> System.out.print(EscapeSequences.WHITE_PAWN);
                case ChessPiece.PieceType.BISHOP -> System.out.print(EscapeSequences.WHITE_BISHOP);
                case ChessPiece.PieceType.ROOK -> System.out.print(EscapeSequences.WHITE_ROOK);
                case ChessPiece.PieceType.KNIGHT -> System.out.print(EscapeSequences.WHITE_KNIGHT);
                case ChessPiece.PieceType.QUEEN -> System.out.print(EscapeSequences.WHITE_QUEEN);
                case ChessPiece.PieceType.KING -> System.out.print(EscapeSequences.WHITE_KING);
            }
        } else {
            currTextColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
            System.out.print(currTextColor);
            switch (currPiece.getPieceType()) {
                case ChessPiece.PieceType.PAWN -> System.out.print(EscapeSequences.BLACK_PAWN);
                case ChessPiece.PieceType.BISHOP -> System.out.print(EscapeSequences.BLACK_BISHOP);
                case ChessPiece.PieceType.ROOK -> System.out.print(EscapeSequences.BLACK_ROOK);
                case ChessPiece.PieceType.KNIGHT -> System.out.print(EscapeSequences.BLACK_KNIGHT);
                case ChessPiece.PieceType.QUEEN -> System.out.print(EscapeSequences.BLACK_QUEEN);
                case ChessPiece.PieceType.KING -> System.out.print(EscapeSequences.BLACK_KING);
            }
        }
    }

    public void printBoardBlack(ChessGame currGame) {
        board = currGame.getBoard();
        perspective = ChessGame.TeamColor.BLACK;

        printHeaderBlack();
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
        System.out.print(HEADER_BG_COLOR);
        System.out.print(" 1 ");
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        currBgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;

        for (int row = 1; row <= 8; row++) {
            currBgColor = (row % 2 == 1) ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_BLACK;
            for (int col = 8; col >= 1; col--) {
                ChessPosition currPos = new ChessPosition(row, col);
                if (highlighting && highlightPosition != null && highlightPosition.contains(currPos)) {
                    System.out.print(currBgColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                            ? EscapeSequences.SET_BG_COLOR_GREEN
                            : EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                } else {
                    System.out.print(currBgColor);
                }

                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                if (currPiece == null) {
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    printCurrSpace(currPiece, col, row);
                }
                currBgColor = currBgColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)
                        ? EscapeSequences.SET_BG_COLOR_BLACK
                        : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            }
            printVerticalNum(row, 1);
            System.out.print(currBgColor);
        }
        printHeaderBlack();
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
    }

    private void printHeader() {
        System.out.print(HEADER_BG_COLOR);
        System.out.print(HEADER_TEXT_COLOR);
        System.out.print(EscapeSequences.EMPTY + " a  b   c   d  e   f  g   h    ");
    }

    private void printHeaderBlack() {
        System.out.print(HEADER_BG_COLOR);
        System.out.print(HEADER_TEXT_COLOR);
        System.out.print(EscapeSequences.EMPTY + " h  g   f   e  d   c  b   a    ");
    }

    private void alternateTileColor(ChessPosition currPos) {

        if (highlighting && highlightPosition != null && highlightPosition.contains(currPos)) {
            // Print highlighted space (choose color based on currBgColor)
            if (currBgColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY) ||
            currBgColor.equals(EscapeSequences.SET_BG_COLOR_GREEN)) {
                // print dark space highlighted
                currBgColor = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
            } else {
                // print light space highlighted
                currBgColor = EscapeSequences.SET_BG_COLOR_GREEN;
            }
        } else {
            if (currBgColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY) ||
            currBgColor.equals(EscapeSequences.SET_BG_COLOR_GREEN)) {
                currBgColor = EscapeSequences.SET_BG_COLOR_BLACK;
            } else {
                currBgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            }
        }
        System.out.print(currBgColor);
    }

    private void printVerticalNum(int currLine, int dir) {
        System.out.print(HEADER_BG_COLOR + HEADER_TEXT_COLOR);
        System.out.print(" " + currLine + " ");
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
        System.out.print(HEADER_BG_COLOR + HEADER_TEXT_COLOR);
        int newLine = currLine + dir;
        if (newLine >= 1 && newLine <= 8) {
            System.out.print(" " + newLine + " ");
        }
    }


    public void highlightPrintBoard(ChessGame currGame, ChessPosition testPos, String color) {
        board = currGame.getBoard();
        if (board.getPiece(testPos) == null) {
            System.out.print("Error: piece not found");
            return;
        }
        Collection<ChessMove> movesToHighlight = new ArrayList<>();
        movesToHighlight = currGame.validMoves(testPos);
        if (movesToHighlight != null && !movesToHighlight.isEmpty()) {
            for (ChessMove validMove : movesToHighlight) {
               highlightPosition.add(validMove.getEndPosition());
            }
        }
        highlighting = true;
        if (color.equals("BLACK")) {
            printBoardBlack(currGame);
        } else {
            printBoardWhite(currGame);
        }
        highlighting = false;
        highlightPosition.clear();
    }




}
