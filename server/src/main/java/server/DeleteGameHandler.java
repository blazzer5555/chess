package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.DeleteGameRequest;
import model.JoinGameRequest;
import org.jetbrains.annotations.NotNull;
import service.DeleteGameService;
import service.JoinGameService;

public class DeleteGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        Gson gson = new Gson();
        DeleteGameRequest request = gson.fromJson(context.body(), DeleteGameRequest.class);
        DeleteGameService joiner = new DeleteGameService();
        String authToken = context.header("authorization");
        try {
            if (joiner.userIsLoggedIn(authToken)) {
                if (joiner.gameExists(request.gameID())) {
                    joiner.removeGame(request.gameID());
                }
                else {
                    ErrorResponder responder = new ErrorResponder();
                    responder.handleBadRequest(context);
                }
            } else {
                ErrorResponder responder = new ErrorResponder();
                responder.handleUnauthorized(context);
            }
        } catch (Exception e) {
            ErrorResponder responder = new ErrorResponder();
            responder.handleBadDatabase(context);
        }
    }
}