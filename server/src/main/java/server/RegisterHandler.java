package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.RegisterService;
import com.google.gson.Gson;

public class RegisterHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        Gson gson = new Gson();
        UserData userData = gson.fromJson(context.body(), UserData.class);
        if (userData.password() == null | userData.username() == null | userData.email() == null) {
            HttpErrorResponder responder = new HttpErrorResponder();
            responder.handleBadRequest(context);
            return;
        }
        try {
            RegisterService registerer = new RegisterService();
            if (registerer.userAlreadyInDatabase(userData.username())) {
                HttpErrorResponder responder = new HttpErrorResponder();
                responder.handleAlreadyTaken(context);
            } else {
                AuthData newAuthData = registerer.registerUser(userData);
                record SuccessfulRegisterResponse(String username, String authToken) {
                }
                SuccessfulRegisterResponse response = new SuccessfulRegisterResponse(newAuthData.username(), newAuthData.authToken());
                context.status(200);
                context.result(gson.toJson(response));
            }
        } catch (Exception e) {
            HttpErrorResponder responder = new HttpErrorResponder();
            responder.handleBadDatabase(context);
        }
    }
}
