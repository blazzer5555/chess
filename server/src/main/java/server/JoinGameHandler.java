package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.JoinGameRequest;
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
        try {
            if (joiner.userIsLoggedIn(authToken)) {
                if (!joiner.isInvalidColor(request.playerColor())) {
                    checkForValidSlot(joiner, context, request, authToken);
                } else {
                    createBadRequestResponse(context);
                }
            } else {
                HttpErrorResponder responder = new HttpErrorResponder();
                responder.handleUnauthorized(context);
            }
        } catch (Exception e) {
            HttpErrorResponder responder = new HttpErrorResponder();
            responder.handleBadDatabase(context);
        }
    }

    private void createBadRequestResponse(Context context) {
        HttpErrorResponder responder = new HttpErrorResponder();
        responder.handleBadRequest(context);
    }

    private void checkForValidSlot(JoinGameService joiner, Context context, JoinGameRequest request, String authToken) {
        try {
            if (joiner.gameExists(request.gameID())) {
                if (joiner.colorAvailable(request.playerColor(), request.gameID())) {
                    joiner.joinGame(request, authToken);
                } else {
                    HttpErrorResponder responder = new HttpErrorResponder();
                    responder.handleAlreadyTaken(context);
                }
            } else {
                createBadRequestResponse(context);
            }
        } catch (Exception e) {
            HttpErrorResponder responder = new HttpErrorResponder();
            responder.handleBadDatabase(context);
        }
    }
}
