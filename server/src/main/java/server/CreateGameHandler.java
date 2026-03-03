package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.CreateGameService;

public class CreateGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        CreateGameService creater = new CreateGameService();
        String authToken = context.header("authorization");
        if (creater.userIsLoggedIn(authToken)) {
            CreateGameRequest request = context.bodyAsClass(CreateGameRequest.class);
            if (creater.gameNameIsTaken(request.gameName())) {
                context.status(400);
                record BadRequestResponse(String message) { }
                BadRequestResponse response = new BadRequestResponse("Error: bad request");
                context.json(response);
            }
            else {
                int newID = creater.createGame(request.gameName());
                context.status(200);
                record SuccessfullyCreatedResponse(int ID) { }
                SuccessfullyCreatedResponse response = new SuccessfullyCreatedResponse(newID);
                context.json(response);
            }
        }
        else {
            context.status(401);
            record UnauthorizedResponse(String message) { }
            UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
            context.json(response);
        }
    }
}
