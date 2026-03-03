package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.JoinGameService;

public class JoinGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        JoinGameService joiner = new JoinGameService();
        String authToken = context.header("authorization");
        if (joiner.userIsLoggedIn(authToken)) {
            JoinGameRequest request = context.bodyAsClass(JoinGameRequest.class);
            if (!joiner.isInvalidColor(request.playerColor())) {
                checkForValidSlot(joiner, context, request, authToken);
            }
            else {
                createBadRequestResponse(context);
            }
        }
        else {
            context.status(401);
            record UnauthorizedResponse(String message) { }
            UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
            context.json(response);
        }
    }

    private void createBadRequestResponse(Context context) {
        context.status(400);
        record BadRequestResponse(String message) { }
        BadRequestResponse response = new BadRequestResponse("Error: bad request");
        context.json(response);
    }

    private void checkForValidSlot(JoinGameService joiner, Context context, JoinGameRequest request, String authToken) {
        if (joiner.gameExists(request.gameID())) {
            if (joiner.colorAvailable(request.playerColor(), request.gameID())) {
                joiner.joinGame(request, authToken);
            }
            else {
                context.status(403);
                record AlreadyTakenResponse(String message) { }
                AlreadyTakenResponse response = new AlreadyTakenResponse("Error: already taken");
                context.json(response);
            }
        }
        else {
            createBadRequestResponse(context);
        }
    }
}
