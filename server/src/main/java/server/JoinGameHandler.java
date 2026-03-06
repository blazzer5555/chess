package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.JoinGameService;
import com.google.gson.Gson;

public class JoinGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(context.body(), JoinGameRequest.class);
        JoinGameService joiner = new JoinGameService();
        String authToken = context.header("authorization");
        if (joiner.userIsLoggedIn(authToken)) {
            if (!joiner.isInvalidColor(request.playerColor())) {
                checkForValidSlot(joiner, context, request, authToken);
            }
            else {
                createBadRequestResponse(context);
            }
        }
        else {
            ErrorResponder responder = new ErrorResponder();
            responder.handleUnauthorized(context, gson);
        }
    }

    private void createBadRequestResponse(Context context) {
        Gson gson = new Gson();
        ErrorResponder responder = new ErrorResponder();
        responder.handleBadRequest(context, gson);
    }

    private void checkForValidSlot(JoinGameService joiner, Context context, JoinGameRequest request, String authToken) {
        Gson gson = new Gson();
        if (joiner.gameExists(request.gameID())) {
            if (joiner.colorAvailable(request.playerColor(), request.gameID())) {
                joiner.joinGame(request, authToken);
            }
            else {
                ErrorResponder responder = new ErrorResponder();
                responder.handleAlreadyTaken(context, gson);
            }
        }
        else {
            createBadRequestResponse(context);
        }
    }
}
