package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {

    ServerMessageType serverMessageType;
    private String errorMessage = "";
    private String message = "";
    private ChessGame game = null;
    private String playerColor = "";

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION,
        HIGHLIGHTED_GAME
    }

    public ServerMessage(ServerMessageType type, String error, String notification, ChessGame game, String color) {
        this.serverMessageType = type;
        this.errorMessage = error;
        this.message = notification;
        this.game = game;
        this.playerColor = color;
    }

    public ServerMessageType getServerMessageType() {
        return serverMessageType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getNotificationMessage() {
        return message;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getColor() {
        return playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
