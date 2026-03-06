package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.LogoutService;
import com.google.gson.Gson;

public class LogoutHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        Gson gson = new Gson();
        LogoutService logoutService = new LogoutService();
        String authToken = context.header("authorization");
        if (logoutService.userIsLoggedIn(authToken)) {
            logoutService.logOutUser(authToken);
            context.status(200);
            context.json("");
        }
        else {
            context.status(401);
            record UnauthorizedResponse(String message) { }
            UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
            context.result(gson.toJson(response));
        }
    }
}
