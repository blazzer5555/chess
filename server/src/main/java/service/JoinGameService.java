package service;

import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import server.JoinGameRequest;

import java.util.Objects;

public class JoinGameService {

    public boolean userIsLoggedIn(String authToken) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameExists(int gameID) {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
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
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        if (Objects.equals(color, "WHITE")) {
            return gameDAO.getGameByID(gameID).whiteUsername() == null;
        }
        else {
            return gameDAO.getGameByID(gameID).blackUsername() == null;
        }
    }

    public void joinGame(JoinGameRequest request, String authToken) {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.updateGame(request, authToken);
    }
}
