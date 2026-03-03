package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.LogoutService;

public class LogoutHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        LogoutService logoutService = new LogoutService();
        String authToken = context.header("authorization");
        if (logoutService.isUserLoggedIn(authToken)) {
            logoutService.logOutUser(authToken);
            context.status(200);
            context.json("");
        }
        else {
            context.status(401);
            record UnauthorizedResponse(String message) { }
            UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
            context.json(response);
        }
    }
}
