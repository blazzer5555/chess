package server;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import model.UserData;
import org.jetbrains.annotations.NotNull;
import service.ClearService;
import service.RegisterService;

public class ClearHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        ClearService clearer = new ClearService();
        clearer.clearDatabase();
        context.status(200);
        context.json("");
    }
}
