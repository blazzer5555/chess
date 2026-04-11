package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import io.javalin.websocket.*;
import model.GameData;
import model.JoinGameRequest;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.swing.text.Position;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnector implements WsMessageHandler, WsConnectHandler, WsCloseHandler {

    final Gson GSON = new Gson();
    private final ConcurrentHashMap<Session, Integer> SESSION_HOLDER = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Session> AUTH_TOKEN_TO_SESSION = new ConcurrentHashMap<>();
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
            else {
                color = "White";
            }
            String notificationMessage = username + " has forfeited the match. " + color + " wins!";
            ServerMessage notifyMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    null, notificationMessage, null, null);
            String serializedMessage = GSON.toJson(notifyMessage);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleMakeMove(WsMessageContext ctx, UserGameCommand command) {
        try {
            GameData gameData = GAME_DAO.getGameByID(command.getGameID());
            try {
                gameData.game().makeMove(command.getMove());
            }
            catch (Exception e) {
                WebsocketErrorResponder er = new WebsocketErrorResponder();
                er.handleErrorResponse(ctx, e.getMessage());
                return;
            }
            GAME_DAO.updateGame(gameData);
            String username = AUTH_DAO.getAuthByAuthToken(command.getAuthToken()).username();
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "WHITE";
            }
            else {
                color = "BLACK";
            }
            ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, null, gameData.game(), color);
            String serializedLoadMessage = GSON.toJson(loadMessage);
            ctx.send(serializedLoadMessage);
            /* String notificationMessage = username + " has just moved a piece from " +
                    command.getMove().getStartPosition().print() + " to " +
                    command.getMove().getEndPosition().print() + ".";
            ServerMessage notifyMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    "", notificationMessage, null, null);
            String serializedMessage = GSON.toJson(notifyMessage);
            ctx.send(serializedMessage); */
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
            Session sessionToDelete = AUTH_TOKEN_TO_SESSION.get(command.getAuthToken());
            AUTH_TOKEN_TO_SESSION.remove(command.getAuthToken());
            SESSION_HOLDER.remove(sessionToDelete);
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
            AUTH_TOKEN_TO_SESSION.put(command.getAuthToken(), ctx.session);
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
}
