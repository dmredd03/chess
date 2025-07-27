package ui;

import ui.EscapeSequences;

public class printGameboard {
    private String CURR_BG_COLOR;
    private String CURR_TEXT_COLOR;
    private String HEADER_BG_COLOR = EscapeSequences.SET_BG_COLOR_RED;
    private String HEADER_TEXT_COLOR = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private String HEADER_SPACE = "\u2003\u2003";

    public void printBoardWhite() {
        printHeader();
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
        System.out.print(HEADER_BG_COLOR);
        System.out.print(" 8 ");
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        CURR_BG_COLOR = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        CURR_TEXT_COLOR = EscapeSequences.SET_TEXT_COLOR_BLUE;
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

        System.out.print(CURR_BG_COLOR + CURR_TEXT_COLOR);
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
        System.out.print(CURR_BG_COLOR + CURR_TEXT_COLOR);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.WHITE_PAWN);
            alternateTileColor();
        }
        alternateTileColor();
        printVerticalNum(2, -1);

        System.out.print(CURR_BG_COLOR + CURR_TEXT_COLOR);
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
    }

    public void printBoardBlack() {
        printHeaderBlack();

        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
        System.out.print(HEADER_BG_COLOR);
        System.out.print(" 1 ");

        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        CURR_BG_COLOR = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        CURR_TEXT_COLOR = EscapeSequences.SET_TEXT_COLOR_RED;

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

        System.out.print(CURR_BG_COLOR + CURR_TEXT_COLOR);
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
        System.out.print(CURR_BG_COLOR + CURR_TEXT_COLOR);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.BLACK_PAWN);
            alternateTileColor();
        }
        alternateTileColor();
        printVerticalNum(7, 1);

        System.out.print(CURR_BG_COLOR + CURR_TEXT_COLOR);
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

    private void alternateTileColor() {
        if (CURR_BG_COLOR.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)) {
            CURR_BG_COLOR = EscapeSequences.SET_BG_COLOR_BLACK;
        } else {
            CURR_BG_COLOR = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        }
        System.out.print(CURR_BG_COLOR);
    }

    private void alternateTeamColor() {
        if (CURR_TEXT_COLOR.equals(EscapeSequences.SET_TEXT_COLOR_BLUE)) {
            CURR_TEXT_COLOR = EscapeSequences.SET_TEXT_COLOR_RED;
        } else {
            CURR_TEXT_COLOR = EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
        System.out.print(CURR_TEXT_COLOR);
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
        System.out.print(CURR_BG_COLOR + CURR_TEXT_COLOR);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.EMPTY);
            alternateTileColor();
        }
        alternateTileColor();
    }


}
