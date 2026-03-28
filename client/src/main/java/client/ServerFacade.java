package client;

import model.*;
import java.util.ArrayList;

public class ServerFacade {

    public static final ServerCommunicator COMMUNICATOR = new ServerCommunicator();

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

    public void sendJoinGameRequest(JoinGameRequest joinGameRequest, String authToken) throws Exception {
        COMMUNICATOR.sendJoinGameRequest(joinGameRequest, authToken);
    }

    public ArrayList<ListGamesResponse> sendListGamesRequest(String authToken) throws Exception {
        return COMMUNICATOR.sendListGamesRequest(authToken);
    }

    public void sendClearRequest(String authToken) throws Exception {
        COMMUNICATOR.sendClearRequest(authToken);
    }
}
