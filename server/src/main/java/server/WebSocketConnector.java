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
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        UserGameCommand command = GSON.fromJson(ctx.message(), UserGameCommand.class);
        Session session = ctx.session;
        sessionHolder.put(0, null);
        ctx.enableAutomaticPings();
        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            DatabaseGameDAO gameDAO = new DatabaseGameDAO();
            GameData gameData = gameDAO.getGameByID(command.getGameID());
            ChessGame game = gameData.game();
            String username = authDAO.getAuthByAuthToken(command.getAuthToken()).username();
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
        //When this receives a message from the client, it checks to see what message it is and does stuff in the database accordingly.
        //Then it sends a message back immediately.
    }
}
