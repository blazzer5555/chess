package server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.ClearService;

public class ClearHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) {
        ClearService clearer = new ClearService();
        try {
            clearer.clearDatabase();
            context.status(200);
            context.result("");
        } catch (Exception e) {
            HttpErrorResponder responder = new HttpErrorResponder();
            responder.handleBadDatabase(context);
        }
    }
}
