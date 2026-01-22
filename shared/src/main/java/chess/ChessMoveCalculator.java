package chess;

import java.util.Collection;
import java.util.*;

public class ChessMoveCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public ChessMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.piece = board.getPiece(position);
    }

    public Collection<ChessMove> calculateMove() {
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return movePawn();
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return moveRook();
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return moveKnight();
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return moveBishop();
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            return moveKing();
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return moveQueen();
        }
        return null;
    }

    private boolean checkSquare(Collection<ChessMove> returnMoves, ChessPosition incrementingPosition) {
        if (board.getPiece(incrementingPosition) == null) {
            returnMoves.add(new ChessMove(position,incrementingPosition,null));
            return false;
        }
        if ((board.getPiece(incrementingPosition) != null)){
            if (board.getPiece(incrementingPosition).getTeamColor() != piece.getTeamColor()) {
                returnMoves.add(new ChessMove(position,incrementingPosition,null));
            }
            return true;
        }
        return true;
    }

    private Collection<ChessMove> movePawn() {
        return null;
    }
    private Collection<ChessMove> moveRook() {
        List<ChessMove> returnMoves = new ArrayList<>();
        int i = 1;
        boolean pieceBlocking = false;
        while (position.getRow() + i <= 8 && !pieceBlocking) {
            ChessPosition incrementingPosition = new ChessPosition(position.getRow() + i, position.getColumn());
            pieceBlocking = checkSquare(returnMoves, incrementingPosition);
            i++;
        }
        i = 1;
        pieceBlocking = false;
        while (position.getRow() - i >= 1 && !pieceBlocking) {
            ChessPosition incrementingPosition = new ChessPosition(position.getRow() - i, position.getColumn());
            pieceBlocking = checkSquare(returnMoves, incrementingPosition);
            i--;
        }
        i = 1;
        pieceBlocking = false;
        while (position.getColumn() + i <= 8 && !pieceBlocking) {
            ChessPosition incrementingPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
            pieceBlocking = checkSquare(returnMoves, incrementingPosition);
            i++;
        }
        i = 1;
        pieceBlocking = false;
        while (position.getColumn() - i >= 1 && !pieceBlocking) {
            ChessPosition incrementingPosition = new ChessPosition(position.getRow(), position.getColumn() - i);
            pieceBlocking = checkSquare(returnMoves, incrementingPosition);
            i++;
        }
        return returnMoves;
    }

    private Collection<ChessMove> moveKnight() {
        return null;
    }

    private Collection<ChessMove> moveBishop() {
        return null;
    }

    private Collection<ChessMove> moveKing() {
        return null;
    }

    private Collection<ChessMove> moveQueen() {
        return null;
    }

}
