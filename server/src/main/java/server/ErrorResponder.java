package server;

import io.javalin.http.Context;
import com.google.gson.Gson;

public class ErrorResponder {

    record ResponseMessage(String message) { }

    public void handleBadRequest(Context context, Gson gson) {
        context.status(400);
        ResponseMessage response = new ResponseMessage("Error: bad request");
        context.result(gson.toJson(response));
    }

    public void handleUnauthorized(Context context, Gson gson) {
        context.status(401);
        ResponseMessage response = new ResponseMessage("Error: unauthorized");
        context.result(gson.toJson(response));
    }

    public void handleAlreadyTaken(Context context, Gson gson) {
        context.status(403);
        ResponseMessage response = new ResponseMessage("Error: already taken");
        context.result(gson.toJson(response));
    }
}
