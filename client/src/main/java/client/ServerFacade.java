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

    public int sendCreateGameRequest(CreateGameRequest createGameRequest) throws Exception {
        return communicator.sendCreateGameRequest(createGameRequest);
    }

    public void sendJoinGameRequest(JoinGameRequest joinGameRequest) throws Exception {
        communicator.sendJoinGameRequest(joinGameRequest);
    }

    public ArrayList<ListGamesReturnData> sendListGamesRequest() throws Exception {
        return communicator.sendListGamesRequest();
    }

    public void sendClearRequest() throws Exception {
        communicator.sendClearRequest();
    }
}
