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

    public boolean gameExists(int gameID) {
        GameDAO gameDAO = new GameDAO();
        return gameDAO.getGameByID(gameID) != null;
    }

    public boolean isInvalidColor(String color) {
        if (color == null) {
            return true;
        }
        else {
            return !color.equals("WHITE") & !color.equals("BLACK");
        }
    }

    public boolean colorAvailable(String color, int gameID) {
        GameDAO gameDAO = new GameDAO();
        if (Objects.equals(color, "WHITE")) {
            return gameDAO.getGameByID(gameID).whiteUsername() == null;
        }
        else {
            return gameDAO.getGameByID(gameID).blackUsername() == null;
        }
    }

    public void joinGame(JoinGameRequest request, String authToken) {
        GameDAO gameDAO = new GameDAO();
        gameDAO.updateGame(request, authToken);
    }
}
