package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WebSocketConnector implements WsMessageHandler {

    final Gson GSON = new Gson();
    Map<Integer, Session> sessionHolder = new HashMap<>();

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        UserGameCommand command = GSON.fromJson(ctx.message(), UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            handleConnect(ctx, command);
        }
        //When this receives a message from the client, it checks to see what message it is and does stuff in the database accordingly.
        //Then it sends a message back immediately.
    }

    private void handleConnect(WsMessageContext ctx, UserGameCommand command) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        try {
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
            try {
                Session session = ctx.session;
                sessionHolder.put(command.getGameID(), session);
                ctx.enableAutomaticPings();
            }
            catch (Exception e) {
                String errorMessage = "There was an error related to the websocket.";
                ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                        errorMessage, "", null, null);
                String serializedMessage = GSON.toJson(message);
                ctx.send(serializedMessage);
                return;
            }
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
            String errorMessage = "There was an error when trying to access the database.";
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                    errorMessage, "", null, null);
            String serializedMessage = GSON.toJson(message);
            ctx.send(serializedMessage);
        }
    }
}
