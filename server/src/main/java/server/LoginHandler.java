package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.jetbrains.annotations.NotNull;
import service.LoginService;
import com.google.gson.Gson;

public class LoginHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(context.body(), LoginRequest.class);
        LoginService loginService = new LoginService();
        if (loginRequest.username() == null | loginRequest.password() == null) {
            ErrorResponder responder = new ErrorResponder();
            responder.handleBadRequest(context);
            return;
        }
        try {
            UserData userData = loginService.getUser(loginRequest);
            if (userData != null) {
                if (loginService.isValidPassword(userData, loginRequest)) {
                    AuthData authData = loginService.logInUser(userData);
                    record LoginResponse(String username, String authToken) {
                    }
                    context.status(200);
                    LoginResponse response = new LoginResponse(userData.username(),
                            authData.authToken());
                    context.result(gson.toJson(response));
                } else {
                    createUnauthorizedResponse(context);
                }
            } else {
                createUnauthorizedResponse(context);
            }
        } catch (Exception e) {
            ErrorResponder responder = new ErrorResponder();
            responder.handleBadDatabase(context);
        }
    }

    private void createUnauthorizedResponse(Context context) {
        ErrorResponder responder = new ErrorResponder();
        responder.handleUnauthorized(context);
    }
}
