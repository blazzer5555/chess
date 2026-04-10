package server;

import chess.ChessGame;
import chess.InvalidMoveException;
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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnector implements WsMessageHandler, WsConnectHandler, WsCloseHandler {

    final Gson GSON = new Gson();
    public final ConcurrentHashMap<Session, Integer> sessionHolder = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Session> authTokenToSession = new ConcurrentHashMap<>();

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        System.out.println("Got to handle message function call.");
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
        //When this receives a message from the client, it checks to see what message it is and does stuff in the database accordingly.
        //Then it sends a message back immediately.
    }

    private void handleMakeMove(WsMessageContext ctx, UserGameCommand command) {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            gameData.game().makeMove(command.getMove());
        }
        catch (InvalidMoveException me) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, me.getMessage());
        }
        catch (Exception e) {

        }
    }

    private void handleLeaving(WsMessageContext ctx, UserGameCommand command) {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
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
            gameDAO.leavePlayer(request);
            Session sessionToDelete = authTokenToSession.get(command.getAuthToken());
            authTokenToSession.remove(command.getAuthToken());
            sessionHolder.remove(sessionToDelete);
            String notificationMessage = "Player " + username + " is no longer playing " + color + ".";
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    "", notificationMessage, null, null);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }

    private void handleConnection(WsMessageContext ctx, UserGameCommand command) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            sessionHolder.put(ctx.session, command.getGameID());
            authTokenToSession.put(command.getAuthToken(), ctx.session);
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "WHITE";
            }
            else {
                color = "BLACK";
            }
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "", "", game, color);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
        }
        catch (Exception e) {
            WebsocketErrorResponder er = new WebsocketErrorResponder();
            er.handleErrorResponse(ctx, "There was an error when trying to access the database.");
        }
    }
}
