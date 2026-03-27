package client;

import static ui.EscapeSequences.*;

public class ChessBoardDrawer {

    public void drawWhitePerspective() {
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print(SET_BG_COLOR_TAN + BLACK_ROOK + SET_BG_COLOR_BROWN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_TAN + BLACK_BISHOP + SET_BG_COLOR_BROWN + BLACK_QUEEN);
        System.out.print(SET_BG_COLOR_TAN + BLACK_KING + SET_BG_COLOR_BROWN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_TAN + BLACK_KNIGHT + SET_BG_COLOR_BROWN + BLACK_ROOK + RESET_BG_COLOR + "\n");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + BLACK_PAWN + SET_BG_COLOR_TAN + BLACK_PAWN);
        }
        System.out.print(RESET_BG_COLOR + "\n");
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 4; i++) {
                System.out.print(SET_BG_COLOR_TAN + EMPTY + SET_BG_COLOR_BROWN + EMPTY);
            }
            System.out.println(RESET_BG_COLOR);
            for (int i = 0; i < 4; i++) {
                System.out.print(SET_BG_COLOR_BROWN + EMPTY + SET_BG_COLOR_TAN + EMPTY);
            }
            System.out.println(RESET_BG_COLOR);
        }
        System.out.print(SET_TEXT_COLOR_WHITE);
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + BLACK_PAWN + SET_BG_COLOR_BROWN + BLACK_PAWN);
        }
        System.out.println(RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_ROOK + SET_BG_COLOR_TAN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_BISHOP + SET_BG_COLOR_TAN + BLACK_QUEEN);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_KING + SET_BG_COLOR_TAN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_KNIGHT + SET_BG_COLOR_TAN + BLACK_ROOK + RESET_BG_COLOR + "\n");
        System.out.print(RESET_TEXT_COLOR);
    }

    public void drawBlackPerspective() {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_TAN + BLACK_ROOK + SET_BG_COLOR_BROWN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_TAN + BLACK_BISHOP + SET_BG_COLOR_BROWN + BLACK_KING);
        System.out.print(SET_BG_COLOR_TAN + BLACK_QUEEN + SET_BG_COLOR_BROWN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_TAN + BLACK_KNIGHT + SET_BG_COLOR_BROWN + BLACK_ROOK + RESET_BG_COLOR + "\n");
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_BROWN + BLACK_PAWN + SET_BG_COLOR_TAN + BLACK_PAWN);
        }
        System.out.print(RESET_BG_COLOR + "\n");
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 4; i++) {
                System.out.print(SET_BG_COLOR_TAN + EMPTY + SET_BG_COLOR_BROWN + EMPTY);
            }
            System.out.println(RESET_BG_COLOR);
            for (int i = 0; i < 4; i++) {
                System.out.print(SET_BG_COLOR_BROWN + EMPTY + SET_BG_COLOR_TAN + EMPTY);
            }
            System.out.println(RESET_BG_COLOR);
        }
        System.out.print(SET_TEXT_COLOR_BLACK);
        for (int i = 0; i < 4; i++) {
            System.out.print(SET_BG_COLOR_TAN + BLACK_PAWN + SET_BG_COLOR_BROWN + BLACK_PAWN);
        }
        System.out.println(RESET_BG_COLOR);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_ROOK + SET_BG_COLOR_TAN + BLACK_KNIGHT);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_BISHOP + SET_BG_COLOR_TAN + BLACK_KING);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_QUEEN + SET_BG_COLOR_TAN + BLACK_BISHOP);
        System.out.print(SET_BG_COLOR_BROWN + BLACK_KNIGHT + SET_BG_COLOR_TAN + BLACK_ROOK + RESET_BG_COLOR + "\n");
        System.out.print(RESET_TEXT_COLOR);
    }
}
