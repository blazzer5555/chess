package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.ListGamesService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class ListGamesHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        Gson gson = new Gson();
        ListGamesService lister = new ListGamesService();
        String authToken = context.header("authorization");
        if (lister.userIsLoggedIn(authToken)) {
            List<GameData> listOfCurrentGames = lister.listCurrentGames();
            record NeededGameInfo(int gameID, String whiteUsername, String blackUsername, String gameName) { }
            record ListResponse(List<NeededGameInfo> games) { }
            ListResponse response = new ListResponse(new ArrayList<>());
            for (GameData game: listOfCurrentGames) {
                NeededGameInfo temp = new NeededGameInfo(game.gameID(), game.whiteUsername(),
                                                         game.blackUsername(), game.gameName());
                response.games.add(temp);
            }
            context.status(200);
            context.result(gson.toJson(response));
        }
        else {
            context.status(401);
            record UnauthorizedResponse(String message) { }
            UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
            context.result(gson.toJson(response));
        }
    }
}
