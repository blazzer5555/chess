package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import model.UserData;
import org.jetbrains.annotations.NotNull;
import service.LoginService;

public class LoginHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        LoginRequest loginRequest = context.bodyAsClass(LoginRequest.class);
        LoginService loginService = new LoginService();
        if (loginRequest.username() == null | loginRequest.password() == null) {
            context.status(400);
            record BadRequestResponse(String message) { }
            BadRequestResponse response = new BadRequestResponse("Error: bad request");
            context.json(response);
            return;
        }
        UserData userData = loginService.getUser(loginRequest);
        if (userData != null) {
            if (loginService.isValidPassword(userData, loginRequest)) {
                AuthData authData = loginService.logInUser(userData);
                record LoginResponse(String username, String authToken) { }
                context.status(200);
                LoginResponse response = new LoginResponse(userData.username(),
                        authData.authToken());
                context.json(response);
            }
            else {
                createUnauthorizedResponse(context);
            }
        }
        else {
            createUnauthorizedResponse(context);
        }
    }

    private void createUnauthorizedResponse(Context context) {
        context.status(401);
        record UnauthorizedResponse(String message) { }
        UnauthorizedResponse response = new UnauthorizedResponse("Error: unauthorized");
        context.json(response);
    }
}
