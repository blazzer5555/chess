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
        //When this receives a message from the client, it checks to see what message it is and does stuff in the database accordingly.
        //Then it sends a message back immediately.
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
            String errorMessage = "There was an error when trying to access the database.";
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    errorMessage, "", null, null);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
        }
    }

    private void handleConnection(WsMessageContext ctx, UserGameCommand command) {
        System.out.println("Got to handleConnection function call.");
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            System.out.println("getGamebyID passed.");
            ChessGame game = gameData.game();
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            System.out.println("getUsername passed.");
            sessionHolder.put(ctx.session, command.getGameID());
            System.out.println("Added session to sessionHolder.");
            authTokenToSession.put(command.getAuthToken(), ctx.session);
            System.out.println("Added authToken session pair.");
            String color;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                color = "WHITE";
            }
            else {
                color = "BLACK";
            }
            System.out.println("Got past the stuff that could trip the try block.");
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "", "", game, color);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
            System.out.println("ctx.send was executed.");
        }
        catch (Exception e) {
            System.out.println("Catch was thrown instead.");
            String errorMessage = "There was an error when trying to access the database.";
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    errorMessage, "", null, null);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
            System.out.println("ctx.send was sent from catch block.");
        }
    }
}
