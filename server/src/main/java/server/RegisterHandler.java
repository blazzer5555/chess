package server;

import com.google.gson.JsonObject;
import dataaccess.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.RegisterService;

import javax.xml.crypto.Data;

public class RegisterHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        UserData userData = context.bodyAsClass(UserData.class);
        RegisterService registerer = new RegisterService();
        try {
            AuthData newAuthData = registerer.registerUser(userData);
            context.status(200);
            record RegisterResponseRecord(String username, String authToken) { }
            RegisterResponseRecord response = new RegisterResponseRecord(userData.username(),
                                                                         newAuthData.authToken());
            context.json(response);
        }
        catch (DataAccessException e){
            context.status(403);
            record UserAlreadyTakenResponse(String message) { }
            UserAlreadyTakenResponse response = new UserAlreadyTakenResponse("Error: already taken");
            context.json(response);
        }
    }
}
