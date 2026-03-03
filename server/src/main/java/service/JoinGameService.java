package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import server.JoinGameRequest;

import java.util.Objects;

public class JoinGameService {

    public boolean userIsLoggedIn(String authToken) {
        AuthDAO authDAO = new AuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameExists(int ID) {
        GameDAO gameDAO = new GameDAO();
        return gameDAO.getGameByID(ID) != null;
    }

    public boolean isInvalidColor(String color) {
        return !color.equals("WHITE") & !color.equals("BLACK");
    }

    public boolean colorAvailable(String color, int ID) {
        GameDAO gameDAO = new GameDAO();
        if (Objects.equals(color, "WHITE")) {
            return gameDAO.getGameByID(ID).whiteUsername() == null;
        }
        else {
            return gameDAO.getGameByID(ID).blackUsername() == null;
        }
    }

    public void joinGame(JoinGameRequest request, String authToken) {
        GameDAO gameDAO = new GameDAO();
        gameDAO.updateGame(request, authToken);
    }
}
