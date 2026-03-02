package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private ClearHandler clearer = new ClearHandler();
    private CreateGameHandler gameCreater = new CreateGameHandler();
    private JoinGameHandler gameJoiner = new JoinGameHandler();
    private ListGamesHandler lister = new ListGamesHandler();
    private LoginHandler loggerIn = new LoginHandler();
    private LogoutHandler loggerOut = new LogoutHandler();
    private RegisterHandler registerer = new RegisterHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));


        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
