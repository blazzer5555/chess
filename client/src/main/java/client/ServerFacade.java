package client;

import chess.ChessMove;
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

    public String sendRegisterRequest(String username, String password, String email) throws Exception {
        UserData registerRequest = new UserData(username, password, email);
        return COMMUNICATOR.sendRegisterRequest(registerRequest);
    }

    public String sendLoginRequest(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);
        return COMMUNICATOR.sendLoginRequest(loginRequest);
    }

    public void sendLogoutRequest(String authToken) throws Exception {
        COMMUNICATOR.sendLogoutRequest(authToken);
    }

    public int sendCreateGameRequest(String gameName, String authToken) throws Exception {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        return COMMUNICATOR.sendCreateGameRequest(createGameRequest, authToken);
    }

    public boolean sendJoinGameRequest(String color, int gameID, String authToken) throws Exception {
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID);
        return COMMUNICATOR.sendJoinGameRequest(joinGameRequest, authToken);
    }

    public ArrayList<ListGamesResponse> sendListGamesRequest(String authToken) throws Exception {
        return COMMUNICATOR.sendListGamesRequest(authToken);
    }

    public void sendClearRequest(String authToken) throws Exception {
        COMMUNICATOR.sendClearRequest(authToken);
    }

    public void sendDeleteGameRequest(String authToken, int gameID) throws Exception {
        DeleteGameRequest deleteGameRequest = new DeleteGameRequest(gameID);
        COMMUNICATOR.sendDeleteGameRequest(deleteGameRequest, authToken);
    }

    public void sendWebsocketRequest(UserGameCommand.CommandType commandType,
                                     String authToken, int gameID, ChessMove move) throws Exception {
        UserGameCommand command = new UserGameCommand(commandType, authToken, gameID, move);
        wsClient.send(command);
    }
}
