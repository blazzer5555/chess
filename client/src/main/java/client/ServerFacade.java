package client;

import model.*;
import websocket.commands.UserGameCommand;

import java.util.ArrayList;

public class ServerFacade {

    public static final ServerCommunicator COMMUNICATOR = new ServerCommunicator();
    public static WebsocketClient wsClient;

    public ServerFacade() {
        try {
            wsClient = new WebsocketClient();
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the websocket connection.");
        }
    }

    public String sendRegisterRequest(UserData registerRequest) throws Exception {
        return COMMUNICATOR.sendRegisterRequest(registerRequest);
    }

    public String sendLoginRequest(LoginRequest loginRequest) throws Exception {
        return COMMUNICATOR.sendLoginRequest(loginRequest);
    }

    public void sendLogoutRequest(String authToken) throws Exception {
        COMMUNICATOR.sendLogoutRequest(authToken);
    }

    public int sendCreateGameRequest(CreateGameRequest createGameRequest, String authToken) throws Exception {
        return COMMUNICATOR.sendCreateGameRequest(createGameRequest, authToken);
    }

    public boolean sendJoinGameRequest(JoinGameRequest joinGameRequest, String authToken) throws Exception {
        return COMMUNICATOR.sendJoinGameRequest(joinGameRequest, authToken);
    }

    public ArrayList<ListGamesResponse> sendListGamesRequest(String authToken) throws Exception {
        return COMMUNICATOR.sendListGamesRequest(authToken);
    }

    public void sendClearRequest(String authToken) throws Exception {
        COMMUNICATOR.sendClearRequest(authToken);
    }

    public void sendDeleteGameRequest(String authToken, DeleteGameRequest deleteGameRequest) throws Exception {
        COMMUNICATOR.sendDeleteGameRequest(deleteGameRequest, authToken);
    }

    public void sendWebsocketRequest(UserGameCommand command) throws Exception {
        wsClient.send(command);
    }
}
