package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import io.javalin.websocket.*;
import model.GameData;
import model.JoinGameRequest;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnector implements WsMessageHandler, WsConnectHandler, WsCloseHandler {

    final Gson GSON = new Gson();
    private final ConcurrentHashMap<Session, Integer> SESSION_HOLDER = new ConcurrentHashMap<>();
    private final DatabaseAuthDAO AUTH_DAO = new DatabaseAuthDAO();
    private final DatabaseGameDAO GAME_DAO = new DatabaseGameDAO();

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        ctx.enableAutomaticPings();
        UserGameCommand command = GSON.fromJson(ctx.message(), UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            handleConnection(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            handleLeaving(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            handleMakeMove(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            handleResign(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.GET_BOARD) {
            handleGetBoard(ctx, command);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.HIGHLIGHT_BOARD) {
            handleHighlightBoard(ctx, command);
        }
    }

    private void handleHighlightBoard(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = GAME_DAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = AUTH_DAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "WHITE";
            }
            else {
                color = "BLACK";
            }
            String position = command.getMove().getStartPosition().print();
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.HIGHLIGHTED_GAME, null, position, game, color);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleGetBoard(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = GAME_DAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = AUTH_DAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "WHITE";
            }
            else {
                color = "BLACK";
            }
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, null, game, color);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleResign(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = GAME_DAO.getGameByID(command.getGameID());
            gameData.game().resign();
            GAME_DAO.updateGame(gameData);
            String username = AUTH_DAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "Black";
            }
            else if (Objects.equals(gameData.blackUsername(), username)) {
                color = "White";
            }
            else {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, "You are observing! You can't resign from a match that you aren't playing.");
                return;
            }
            for (Session session: SESSION_HOLDER.keySet()) {
                if (Objects.equals(SESSION_HOLDER.get(session), command.getGameID())) {
                    String notificationMessage = username + " has forfeited the match. " + color + " wins!";
                    ServerMessage notifyMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            null, notificationMessage, null, null);
                    String serializedMessage = GSON.toJson(notifyMessage);
                    session.getRemote().sendString(serializedMessage);
                }
            }
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleMakeMove(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = GAME_DAO.getGameByID(command.getGameID());
            String username = AUTH_DAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            ChessGame.TeamColor enumColor;
            String oppositeColor;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "WHITE";
                oppositeColor = "Black";
                enumColor = ChessGame.TeamColor.BLACK;
                if (gameData.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor()
                        == ChessGame.TeamColor.BLACK) {
                    WebsocketErrorResponder er = new WebsocketErrorResponder();
                    er.handleErrorResponse(ctx, "You can't move another person's piece!");
                    return;
                }
            }
            else if (Objects.equals(gameData.blackUsername(), username)) {
                color = "BLACK";
                oppositeColor = "White";
                enumColor = ChessGame.TeamColor.WHITE;
                if (gameData.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor()
                        == ChessGame.TeamColor.WHITE) {
                    WebsocketErrorResponder er = new WebsocketErrorResponder();
                    er.handleErrorResponse(ctx, "You can't move another person's piece!");
                    return;
                }
            }
            else {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, "You are observing! You can't make any moves.");
                return;
            }
            try {
                gameData.game().makeMove(command.getMove());
            }
            catch (Exception e) {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, e.getMessage());
                return;
            }
            if (gameData.game().isInCheckmate(enumColor) || gameData.game().isInStalemate(enumColor)) {
                gameData.game().resign();
            }
            GAME_DAO.updateGame(gameData);
            for (Session session: SESSION_HOLDER.keySet()) {
                if (Objects.equals(SESSION_HOLDER.get(session), command.getGameID())) {
                    ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                            null, null, gameData.game(), color);
                    String serializedMessage = GSON.toJson(loadGameMessage);
                    session.getRemote().sendString(serializedMessage);
                    if (session != ctx.session) {
                        String notificationString = username + " has made a move from " + command.getMove().getStartPosition().print() +
                                " to " + command.getMove().getEndPosition().print();
                        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                null, notificationString, null, null);
                        String otherSerializedMessage = GSON.toJson(notificationMessage);
                        session.getRemote().sendString(otherSerializedMessage);
                    }
                    if (gameData.game().isInCheckmate(enumColor)) {
                        notifyCheckmate(username, session, oppositeColor);
                    }
                    else if (gameData.game().isInCheck(enumColor)) {
                        notifyCheck(session, oppositeColor);
                    }
                    else if (gameData.game().isInStalemate(enumColor)) {
                        notifyStalemate(session, oppositeColor);
                    }
                }
            }
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleLeaving(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = GAME_DAO.getGameByID(command.getGameID());
            String username = AUTH_DAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            String enumColor;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "white";
                enumColor = "WHITE";
            }
            else {
                color = "black";
                enumColor = "BLACK";
            }
            JoinGameRequest request = new JoinGameRequest(enumColor, command.getGameID());
            GAME_DAO.leavePlayer(request);
            SESSION_HOLDER.remove(ctx.session);
            String notificationMessage = "Player " + username + " is no longer playing " + color + ".";
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    null, notificationMessage, null, null);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleConnection(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = GAME_DAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = AUTH_DAO.getAuthByAuthToken(command.getAuthToken()).username();
            SESSION_HOLDER.put(ctx.session, command.getGameID());
            String enumColor;
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                enumColor = "WHITE";
                color = "white";
            }
            else {
                enumColor = "BLACK";
                color = "black";
            }
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                    null, null, game, enumColor);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
            for (Session session: SESSION_HOLDER.keySet()) {
                if ((Objects.equals(SESSION_HOLDER.get(session), command.getGameID())) && (session != ctx.session)) {
                    String notificationMessage = "Player " + username + " has joined the game as " + color + ".";
                    ServerMessage otherClientMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            null, notificationMessage, null, null);
                    String otherSerializedMessage = GSON.toJson(otherClientMessage);
                    session.getRemote().sendString(otherSerializedMessage);
                }
            }
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void notifyCheck(Session session, String color) throws Exception {
        String notificationString = color + " is in check!";
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                null, notificationString, null, null);
        String otherSerializedMessage = GSON.toJson(notificationMessage);
        session.getRemote().sendString(otherSerializedMessage);
    }

    private void notifyCheckmate(String username, Session session, String color) throws Exception {
        String notificationString = color + " has been checkmated. " + username + " wins!";
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                null, notificationString, null, null);
        String otherSerializedMessage = GSON.toJson(notificationMessage);
        session.getRemote().sendString(otherSerializedMessage);
    }

    private void notifyStalemate(Session session, String color) throws Exception {
        String notificationString = color + " is in stalemate. The game ends in a draw.";
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                null, notificationString, null, null);
        String otherSerializedMessage = GSON.toJson(notificationMessage);
        session.getRemote().sendString(otherSerializedMessage);
    }
}
