package client;

import static ui.EscapeSequences.*;

public class ChessBoardDrawer {

    public void drawWhitePerspective() {
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print(SET_BG_COLOR_PALE_TAN + "    a   b   c  d   e  f   g   h    " + RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_PALE_TAN + " 8 ");
        System.out.print(SET_BG_COLOR_TAN + BLACK_ROOK + SET_BG_COLOR_BROWN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_TAN + BLACK_BISHOP + SET_BG_COLOR_BROWN + BLACK_QUEEN);
        System.out.print(SET_BG_COLOR_TAN + BLACK_KING + SET_BG_COLOR_BROWN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_TAN + BLACK_KNIGHT + SET_BG_COLOR_BROWN + BLACK_ROOK + SET_BG_COLOR_PALE_TAN + " 8 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_PALE_TAN + " 7 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + BLACK_PAWN + SET_BG_COLOR_TAN + BLACK_PAWN);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 7 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 6 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + EMPTY + SET_BG_COLOR_BROWN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 6 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 5 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + EMPTY + SET_BG_COLOR_TAN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 5 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 4 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + EMPTY + SET_BG_COLOR_BROWN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 4 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 3 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + EMPTY + SET_BG_COLOR_TAN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 3 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 2 ");
        System.out.print(SET_TEXT_COLOR_WHITE);
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + BLACK_PAWN + SET_BG_COLOR_BROWN + BLACK_PAWN);
        }
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_PALE_TAN + " 2 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 1 ");
        System.out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_BROWN + BLACK_ROOK + SET_BG_COLOR_TAN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_BISHOP + SET_BG_COLOR_TAN + BLACK_QUEEN);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_KING + SET_BG_COLOR_TAN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_KNIGHT + SET_BG_COLOR_TAN + BLACK_ROOK);
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_PALE_TAN + " 1 " + RESET_BG_COLOR);
        System.out.println(SET_BG_COLOR_PALE_TAN + "    a   b   c  d   e  f   g   h    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
    }

    public void drawBlackPerspective() {
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print(SET_BG_COLOR_PALE_TAN + "    h   g   f  e   d  c   b   a    " + RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_PALE_TAN + " 1 " + SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_TAN + BLACK_ROOK + SET_BG_COLOR_BROWN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_TAN + BLACK_BISHOP + SET_BG_COLOR_BROWN + BLACK_KING);
        System.out.print(SET_BG_COLOR_TAN + BLACK_QUEEN + SET_BG_COLOR_BROWN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_TAN + BLACK_KNIGHT + SET_BG_COLOR_BROWN + BLACK_ROOK + SET_BG_COLOR_PALE_TAN);
        System.out.println(SET_TEXT_COLOR_BLACK + " 1 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 2 " + SET_TEXT_COLOR_WHITE);
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + BLACK_PAWN + SET_BG_COLOR_TAN + BLACK_PAWN);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 2 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 3 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + EMPTY + SET_BG_COLOR_BROWN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 3 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 4 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + EMPTY + SET_BG_COLOR_TAN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 4 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 5 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + EMPTY + SET_BG_COLOR_BROWN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 5 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 6 ");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + EMPTY + SET_BG_COLOR_TAN + EMPTY);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 6 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 7 ");
        System.out.print(SET_TEXT_COLOR_BLACK);
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + BLACK_PAWN + SET_BG_COLOR_BROWN + BLACK_PAWN);
        }
        System.out.println(SET_BG_COLOR_PALE_TAN + " 7 " + RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_PALE_TAN + " 8 ");
        System.out.print(SET_BG_COLOR_BROWN + BLACK_ROOK + SET_BG_COLOR_TAN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_BISHOP + SET_BG_COLOR_TAN + BLACK_KING);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_QUEEN + SET_BG_COLOR_TAN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_KNIGHT + SET_BG_COLOR_TAN + BLACK_ROOK);
        System.out.println(SET_BG_COLOR_PALE_TAN + " 8 " + RESET_BG_COLOR);
        System.out.println(SET_BG_COLOR_PALE_TAN + "    h   g   f  e   d  c   b   a    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
    }
}
