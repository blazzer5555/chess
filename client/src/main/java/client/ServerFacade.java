package client;

import model.*;
import java.util.ArrayList;

public class ServerFacade {

    public static final ServerCommunicator communicator = new ServerCommunicator();

    public void sendRegisterRequest(UserData registerRequest) {
        communicator.sendRegisterRequest(registerRequest);
    }

    public String sendLoginRequest(LoginRequest loginRequest) {
        communicator.sendLoginRequest(loginRequest);
        return "";
    }

    public void sendLogoutRequest(String authToken) {
        communicator.sendLogoutRequest(authToken);
    }

    public void sendCreateGameRequest(CreateGameRequest createGameRequest) {
        communicator.sendCreateGameRequest(createGameRequest);
    }

    public void sendJoinGameRequest(JoinGameRequest joinGameRequest) {
        communicator.sendJoinGameRequest(joinGameRequest);
    }

    public ArrayList<ListGamesReturnData> sendListGamesRequest() {
        return communicator.sendListGamesRequest();
    }

    public void sendClearRequest() {
        communicator.sendClearRequest();
    }
}
