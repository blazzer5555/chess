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
        try {
            if (logoutService.userIsLoggedIn(authToken)) {
                logoutService.logOutUser(authToken);
                context.status(200);
                context.json("");
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
