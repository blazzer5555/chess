package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.ListGamesService;

import java.util.ArrayList;
import java.util.List;

public class ListGamesHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        ListGamesService lister = new ListGamesService();
        String authToken = context.header("authorization");
        if (lister.isUserLoggedIn(authToken)) {
            List<GameData> listOfCurrentGames = lister.listCurrentGames();
            record ListResponse(List<GameData> games) { }
            ListResponse response = new ListResponse(listOfCurrentGames);
            context.status(200);
            context.json(response);
        }
        else {
            context.status(401);
            record UnauthorizedResponse(String message) { }
            UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
            context.json(response);
        }
    }
}
