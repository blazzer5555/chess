package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessBoardDrawer {

    public void drawWhitePerspective(ChessBoard board) {
        System.out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_PALE_TAN + "    a   b   c  d   e  f   g   h    " +
                RESET_BG_COLOR + "\n" + SET_BG_COLOR_PALE_TAN + " 8 ");
        boolean lightSpace = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(8, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 8 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 7 ");
        lightSpace = false;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(7, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 7 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 6 ");
        lightSpace = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(6, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 6 " +
                RESET_BG_COLOR + "\n" + SET_BG_COLOR_PALE_TAN + " 5 ");
        lightSpace = false;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(5, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 5 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 4 ");
        lightSpace = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(4, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 4 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 3 ");
        lightSpace = false;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(3, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 3 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 2 ");
        lightSpace = true;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(2, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 2 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 1 ");
        lightSpace = false;
        for (int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(1, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_PALE_TAN + " 1 " + RESET_BG_COLOR);
        System.out.println(SET_BG_COLOR_PALE_TAN + "    a   b   c  d   e  f   g   h    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
    }

    public void drawBlackPerspective(ChessBoard board) {
        System.out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_PALE_TAN + "    h   g   f  e   d  c   b   a    " +
                RESET_BG_COLOR + "\n" + SET_BG_COLOR_PALE_TAN + " 1 ");
        boolean lightSpace = true;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(1, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 1 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 2 ");
        lightSpace = false;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(2, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 2 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 3 ");
        lightSpace = true;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(3, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 3 " +
                RESET_BG_COLOR + "\n" + SET_BG_COLOR_PALE_TAN + " 4 ");
        lightSpace = false;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(4, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 4 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 5 ");
        lightSpace = true;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(5, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 5 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 6 ");
        lightSpace = false;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(6, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 6 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 7 ");
        lightSpace = true;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(7, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.print(SET_BG_COLOR_PALE_TAN + SET_TEXT_COLOR_BLACK + " 7 " + RESET_BG_COLOR +
                "\n" + SET_BG_COLOR_PALE_TAN + " 8 ");
        lightSpace = false;
        for (int i = 8; i > 0; i--) {
            ChessPosition position = new ChessPosition(8, i);
            ChessPiece piece = board.getPiece(position);
            lightSpace = printSquare(piece, lightSpace);
        }
        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_PALE_TAN + " 8 " + RESET_BG_COLOR);
        System.out.println(SET_BG_COLOR_PALE_TAN + "    h   g   f  e   d  c   b   a    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
    }

    private boolean printSquare(ChessPiece piece, boolean lightSpace) {
        String printStatement = "";
        if (piece == null) {
            if (lightSpace) {
                printStatement += SET_BG_COLOR_TAN + SET_TEXT_COLOR_TAN + BLACK_PAWN;
                lightSpace = false;
                System.out.print(printStatement);
            }
            else {
                printStatement += SET_BG_COLOR_BROWN + SET_TEXT_COLOR_BROWN + BLACK_PAWN;
                lightSpace = true;
                System.out.print(printStatement);
            }
            return lightSpace;
        }
        if (lightSpace) {
            printStatement += SET_BG_COLOR_TAN;
            lightSpace = false;
        }
        else {
            printStatement += SET_BG_COLOR_BROWN;
            lightSpace = true;
        }
        printStatement += getCodesFromPiece(piece);
        System.out.print(printStatement);
        return lightSpace;
    }

    private String getCodesFromPiece(ChessPiece piece) {
        String terminalCodes = "";
        ChessPiece.PieceType pieceType = piece.getPieceType();
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        if (teamColor == ChessGame.TeamColor.WHITE) {
            terminalCodes += SET_TEXT_COLOR_WHITE;
        }
        else if (teamColor == ChessGame.TeamColor.BLACK) {
            terminalCodes += SET_TEXT_COLOR_BLACK;
        }
        if (pieceType == ChessPiece.PieceType.PAWN) {
            terminalCodes += BLACK_PAWN;
        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {
            terminalCodes += BLACK_ROOK;
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT) {
            terminalCodes += BLACK_KNIGHT;
        }
        else if (pieceType == ChessPiece.PieceType.BISHOP) {
            terminalCodes += BLACK_BISHOP;
        }
        else if (pieceType == ChessPiece.PieceType.KING) {
            terminalCodes += BLACK_KING;
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN) {
            terminalCodes += BLACK_QUEEN;
        }
        return terminalCodes;
    }
}
