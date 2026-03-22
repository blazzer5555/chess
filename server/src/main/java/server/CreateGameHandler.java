package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.CreateGameService;

public class CreateGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(context.body(), CreateGameRequest.class);
        CreateGameService creater = new CreateGameService();
        String authToken = context.header("authorization");
        try {
            if (creater.userIsLoggedIn(authToken)) {
                if (request.gameName() == null | creater.gameNameIsTaken(request.gameName())) {
                    ErrorResponder responder = new ErrorResponder();
                    responder.handleBadRequest(context);
                } else {
                    int newID = creater.createGame(request.gameName());
                    context.status(200);
                    record SuccessfullyCreatedResponse(int gameID) {
                    }
                    SuccessfullyCreatedResponse response = new SuccessfullyCreatedResponse(newID);
                    context.result(gson.toJson(response));
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
