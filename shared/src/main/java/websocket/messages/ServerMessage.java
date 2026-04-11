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
    private final String ERROR_MESSAGE;
    private final String MESSAGE;
    private final ChessGame GAME;
    private final String PLAYER_COLOR;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION,
        HIGHLIGHTED_GAME
    }

    public ServerMessage(ServerMessageType type, String error, String notification, ChessGame game, String color) {
        this.serverMessageType = type;
        this.ERROR_MESSAGE = error;
        this.MESSAGE = notification;
        this.GAME = game;
        this.PLAYER_COLOR = color;
    }

    public ServerMessageType getServerMessageType() {
        return serverMessageType;
    }

    public String getErrorMessage() {
        return ERROR_MESSAGE;
    }

    public String getNotificationMessage() {
        return MESSAGE;
    }

    public ChessGame getGame() {
        return GAME;
    }

    public String getColor() {
        return PLAYER_COLOR;
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
