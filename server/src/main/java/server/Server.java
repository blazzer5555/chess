package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private ClearHandler clearer = new ClearHandler();
    private final CreateGameHandler gameCreater = new CreateGameHandler();
    private JoinGameHandler gameJoiner = new JoinGameHandler();
    private final ListGamesHandler lister = new ListGamesHandler();
    private final LoginHandler loggerIn = new LoginHandler();
    private final LogoutHandler loggerOut = new LogoutHandler();
    private final RegisterHandler registerer = new RegisterHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", registerer);
        javalin.post("/session", loggerIn);
        javalin.delete("/session", loggerOut);
        javalin.get("/game", lister);
        javalin.post("/game", gameCreater);
        javalin.put("/game", gameJoiner);
        javalin.delete("/db", clearer);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
