package client;

import model.*;
import java.util.ArrayList;

public class ServerFacade {

    public static final ServerCommunicator communicator = new ServerCommunicator();

    public String sendRegisterRequest(UserData registerRequest) throws Exception {
        return communicator.sendRegisterRequest(registerRequest);
    }

    public String sendLoginRequest(LoginRequest loginRequest) throws Exception {
        return communicator.sendLoginRequest(loginRequest);
    }

    public void sendLogoutRequest(String authToken) throws Exception {
        communicator.sendLogoutRequest(authToken);
    }

    public int sendCreateGameRequest(CreateGameRequest createGameRequest, String authToken) throws Exception {
        return communicator.sendCreateGameRequest(createGameRequest, authToken);
    }

    public void sendJoinGameRequest(JoinGameRequest joinGameRequest, String authToken) throws Exception {
        communicator.sendJoinGameRequest(joinGameRequest, authToken);
    }

    public ArrayList<ListGamesResponse> sendListGamesRequest(String authToken) throws Exception {
        return communicator.sendListGamesRequest(authToken);
    }

    public void sendClearRequest(String authToken) throws Exception {
        communicator.sendClearRequest(authToken);
    }
}
