package server;

import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;

public class WebSocketConnector implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {

    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        ctx.send(ctx.message());
        //When this receives a message from the client, it checks to see what message it is and does stuff in the database accordingly.
        //Then it sends a message back immediately.
    }
}
