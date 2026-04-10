package client;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class WebsocketClient extends Endpoint{

    public Session session;
    private final Gson GSON = new Gson();
    private final ChessBoardDrawer DRAWER = new ChessBoardDrawer();

    public WebsocketClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
            ServerMessage deserializedMessage = GSON.fromJson(message, ServerMessage.class);
            if (deserializedMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                if (Objects.equals(deserializedMessage.getColor(), "WHITE")) {
                    DRAWER.drawWhitePerspective(deserializedMessage.getGame().getBoard());
                }
                else {
                    DRAWER.drawBlackPerspective(deserializedMessage.getGame().getBoard());
                }
            }
            if (deserializedMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                System.out.println(deserializedMessage.getNotificationMessage());
            }
            //When this receives a message from the server, check what the message is, then do the appropriate action.
        });
    }

    public void send(UserGameCommand command) throws IOException {
        String message = GSON.toJson(command);
        session.getBasicRemote().sendText(message);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
