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
        if (creater.userIsLoggedIn(authToken)) {
            if  (request.gameName() == null | creater.gameNameIsTaken(request.gameName())) {
                context.status(400);
                record BadRequestResponse(String message) { }
                BadRequestResponse response = new BadRequestResponse("Error: bad request");
                context.result(gson.toJson(response));
            }
            else {
                int newID = creater.createGame(request.gameName());
                context.status(200);
                record SuccessfullyCreatedResponse(int gameID) { }
                SuccessfullyCreatedResponse response = new SuccessfullyCreatedResponse(newID);
                context.result(gson.toJson(response));
            }
        }
        else {
            context.status(401);
            record UnauthorizedResponse(String message) { }
            UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
            context.result(gson.toJson(response));
        }
    }
}
