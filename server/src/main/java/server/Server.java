package server;

import dataaccess.*;
import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        WebSocketConnector connector = new WebSocketConnector();
        javalin.ws("/ws", ws -> {
                    ws.onConnect(connector);
                    ws.onMessage(connector);
                    ws.onClose(connector);
                });
        RegisterHandler registerer = new RegisterHandler();
        javalin.post("/user", registerer);
        LoginHandler loggerIn = new LoginHandler();
        javalin.post("/session", loggerIn);
        LogoutHandler loggerOut = new LogoutHandler();
        javalin.delete("/session", loggerOut);
        ListGamesHandler lister = new ListGamesHandler();
        javalin.get("/game", lister);
        CreateGameHandler gameCreater = new CreateGameHandler();
        javalin.post("/game", gameCreater);
        JoinGameHandler gameJoiner = new JoinGameHandler();
        javalin.put("/game", gameJoiner);
        ClearHandler clearer = new ClearHandler();
        javalin.delete("/db", clearer);
        DeleteGameHandler deleter = new DeleteGameHandler();
        javalin.put("/g", deleter);
    }

    public int run(int desiredPort) {
        try {
            initializeDatabase();
        }
        catch (DataAccessException e) {
            System.out.println("Something went wrong creating the database. Terminating server.");
            return 8080;
        }
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void initializeDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}
