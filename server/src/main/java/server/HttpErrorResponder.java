package server;

import io.javalin.http.Context;
import com.google.gson.Gson;

public class HttpErrorResponder {

    record ResponseMessage(String message) { }
    private final Gson gson = new Gson();

    public void handleBadRequest(Context context) {
        context.status(400);
        ResponseMessage response = new ResponseMessage("Error: bad request");
        context.result(gson.toJson(response));
    }

    public void handleUnauthorized(Context context) {
        context.status(401);
        ResponseMessage response = new ResponseMessage("Error: unauthorized");
        context.result(gson.toJson(response));
    }

    public void handleAlreadyTaken(Context context) {
        context.status(403);
        ResponseMessage response = new ResponseMessage("Error: already taken");
        context.result(gson.toJson(response));
    }

    public void handleBadDatabase(Context context) {
        context.status(500);
        ResponseMessage response = new ResponseMessage("Error: database");
        context.result(gson.toJson(response));
    }
}
