package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.RegisterService;

public class RegisterHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        UserData userData = context.bodyAsClass(UserData.class);
        if (userData.password() == null | userData.username() == null | userData.email() == null) {
            context.status(400);
            record BadRequestResponse(String message) { }
            BadRequestResponse response = new BadRequestResponse("Error: bad request");
            context.json(response);
            return;
        }
        RegisterService registerer = new RegisterService();
        if (registerer.userAlreadyInDatabase(userData.username())) {
            context.status(403);
            record UserAlreadyTakenResponse(String message) { }
            UserAlreadyTakenResponse response = new UserAlreadyTakenResponse("Error: already taken");
            context.json(response);
        }
        else {
            AuthData newAuthData = registerer.registerUser(userData);
            record SuccessfulRegisterResponse(String username, String authToken) { }
            SuccessfulRegisterResponse response = new SuccessfulRegisterResponse(newAuthData.username(), newAuthData.authToken());
            context.status(200);
            context.json(response);
        }
    }
}
