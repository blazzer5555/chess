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
        UserData userData = loginService.getUser(loginRequest);
        if (userData != null) {
            if (loginService.isAlreadyLoggedIn(userData)) {
                createBadRequestResponse(context);
            }
            else {
                if (loginService.isValidPassword(userData, loginRequest)) {
                    AuthData authData = loginService.logInUser(userData);
                    record LoginResponseRecord(String username, String authToken) {
                    }
                    context.status(200);
                    LoginResponseRecord response = new LoginResponseRecord(userData.username(),
                            authData.authToken());
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
        else {
            createBadRequestResponse(context);
        }
    }

    private void createBadRequestResponse(Context context) {
        context.status(400);
        record BadRequestResponse(String message) { }
        BadRequestResponse response = new BadRequestResponse("Error: bad request");
        context.json(response);
    }
}
