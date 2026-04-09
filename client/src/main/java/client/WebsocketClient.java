package client;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;

public class WebsocketClient {
    public Session session;

    public WebsocketClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                //When this receives a message from the server, check what the message is, then do the appropriate action.
            }
        });
    }

    public void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
        //This sends a message to the server
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
