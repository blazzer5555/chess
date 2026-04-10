package server;

import com.google.gson.Gson;
import io.javalin.websocket.WsMessageContext;
import websocket.messages.ServerMessage;

public class WebsocketErrorResponder {

    private final Gson GSON = new Gson();

    public void handleErrorResponse(WsMessageContext ctx, String errorMessage) {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                errorMessage, "", null, null);
        String serializedMessage = GSON.toJson(message);
        ctx.send(serializedMessage);
    }
}
