package ui;

import chess.*;

public class PrintGameBoard {
    private static String currBgColor;
    private static String currTextColor;
    private static final String HEADER_BG_COLOR = EscapeSequences.SET_BG_COLOR_RED;
    private static final String HEADER_TEXT_COLOR = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private ChessBoard board;
    private ChessGame.TeamColor perspective;

/*    public void printBoardWhite() {
        printHeader();
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
        System.out.print(HEADER_BG_COLOR);
        System.out.print(" 8 ");
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        currBgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        currTextColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        System.out.print(EscapeSequences.BLACK_ROOK);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_QUEEN);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_KING);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_ROOK);

        printVerticalNum(8, -1);

        System.out.print(currBgColor + currTextColor);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.BLACK_PAWN);
            alternateTileColor();
        }
        alternateTileColor();

        printVerticalNum(7, -1);
        printBlankLine();
        printVerticalNum(6, -1);
        printBlankLine();
        printVerticalNum(5, -1);
        printBlankLine();
        printVerticalNum(4, -1);
        printBlankLine();
        printVerticalNum(3, -1);

        alternateTeamColor();
        System.out.print(currBgColor + currTextColor);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.WHITE_PAWN);
            alternateTileColor();
        }
        alternateTileColor();
        printVerticalNum(2, -1);

        System.out.print(currBgColor + currTextColor);
        System.out.print(EscapeSequences.WHITE_ROOK);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_QUEEN);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_KING);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_ROOK);

        printVerticalNum(1, -1);
        printHeader();

        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }*/

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
            for (int col = 1; col <= 8; col++) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                if (currPiece == null) {
                    printCurrBlankSpace(col);
                } else {
                    printCurrSpace(currPiece, col);
                }
            }
            printVerticalNum(row, -1);
        }
        printHeader();
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }

    private void printCurrSpace(ChessPiece currPiece, int col) {
        if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            currTextColor = EscapeSequences.SET_TEXT_COLOR_RED;
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
            switch (currPiece.getPieceType()) {
                case ChessPiece.PieceType.PAWN -> System.out.print(EscapeSequences.BLACK_PAWN);
                case ChessPiece.PieceType.BISHOP -> System.out.print(EscapeSequences.BLACK_BISHOP);
                case ChessPiece.PieceType.ROOK -> System.out.print(EscapeSequences.BLACK_ROOK);
                case ChessPiece.PieceType.KNIGHT -> System.out.print(EscapeSequences.BLACK_KNIGHT);
                case ChessPiece.PieceType.QUEEN -> System.out.print(EscapeSequences.BLACK_QUEEN);
                case ChessPiece.PieceType.KING -> System.out.print(EscapeSequences.BLACK_KING);
            }
        }

        if ((col != 8 && perspective.equals(ChessGame.TeamColor.WHITE))) {
            alternateTileColor();
        } else if (col != 1 && perspective.equals(ChessGame.TeamColor.BLACK)) {
            alternateTileColor();
        }
    }

    private void printCurrBlankSpace(int col){
        System.out.print(EscapeSequences.EMPTY);
        if (col != 8 && perspective.equals(ChessGame.TeamColor.WHITE)) {
            alternateTileColor();
        } else if (col != 1 && perspective.equals(ChessGame.TeamColor.BLACK)) {
            alternateTileColor();
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
            for (int col = 8; col >= 1; col--) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                if (currPiece == null) {
                    printCurrBlankSpace(col);
                } else {
                    printCurrSpace(currPiece, col);
                }
            }
            printVerticalNum(row, 1);
        }
        printHeaderBlack();
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }

    /*public void printBoardBlack() {
        printHeaderBlack();

        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
        System.out.print(HEADER_BG_COLOR);
        System.out.print(" 1 ");

        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        currBgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        currTextColor = EscapeSequences.SET_TEXT_COLOR_RED;

        System.out.print(EscapeSequences.WHITE_ROOK);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_KING);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_QUEEN);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.WHITE_ROOK);

        printVerticalNum(1, 1);

        System.out.print(currBgColor + currTextColor);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.WHITE_PAWN);
            alternateTileColor();
        }
        alternateTileColor();

        printVerticalNum(2, 1);
        printBlankLine();
        printVerticalNum(3, 1);
        printBlankLine();
        printVerticalNum(4, 1);
        printBlankLine();
        printVerticalNum(5, 1);
        printBlankLine();
        printVerticalNum(6, 1);

        alternateTeamColor();
        System.out.print(currBgColor + currTextColor);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.BLACK_PAWN);
            alternateTileColor();
        }
        alternateTileColor();
        printVerticalNum(7, 1);

        System.out.print(currBgColor + currTextColor);
        // Black pieces, reversed order
        System.out.print(EscapeSequences.BLACK_ROOK);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_KING); // King and Queen swapped
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_QUEEN);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_BISHOP);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_KNIGHT);
        alternateTileColor();
        System.out.print(EscapeSequences.BLACK_ROOK);

        printVerticalNum(8, 1);
        printHeaderBlack();

        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);


    }*/

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

    private void alternateTileColor() {
        if (currBgColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)) {
            currBgColor = EscapeSequences.SET_BG_COLOR_BLACK;
        } else {
            currBgColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        }
        System.out.print(currBgColor);
    }

    private void alternateTeamColor() {
        if (currTextColor.equals(EscapeSequences.SET_TEXT_COLOR_BLUE)) {
            currTextColor = EscapeSequences.SET_TEXT_COLOR_RED;
        } else {
            currTextColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
        System.out.print(currTextColor);
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

    private void printBlankLine() {
        System.out.print(currBgColor + currTextColor);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.EMPTY);
            alternateTileColor();
        }
        alternateTileColor();
    }


}
