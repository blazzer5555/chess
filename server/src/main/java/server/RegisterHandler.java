package server;

import dataaccess.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.RegisterService;

public class RegisterHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        UserData userData = context.bodyAsClass(UserData.class);
        RegisterService registerer = new RegisterService();
        if (registerer.userAlreadyInDatabase(userData.username())) {
            context.status(403);
            record UserAlreadyTakenResponse(String message) { }
            UserAlreadyTakenResponse response = new UserAlreadyTakenResponse("Error: already taken");
            context.json(response);
        }
        else {
            AuthData newAuthToken = registerer.registerUser(userData);
            record SuccessfulRegisterResponse(String authToken) { }
            SuccessfulRegisterResponse response = new SuccessfulRegisterResponse(newAuthToken.authToken());
            context.status(200);
            context.json(response);
        }
    }
}
