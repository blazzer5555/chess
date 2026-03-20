package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import server.JoinGameRequest;

import java.util.Objects;

public class JoinGameService {

    public boolean userIsLoggedIn(String authToken) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameExists(int gameID) {
        MemoryGameDAO gameDAO = new MemoryGameDAO();
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
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        if (Objects.equals(color, "WHITE")) {
            return gameDAO.getGameByID(gameID).whiteUsername() == null;
        }
        else {
            return gameDAO.getGameByID(gameID).blackUsername() == null;
        }
    }

    public void joinGame(JoinGameRequest request, String authToken) {
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        gameDAO.updateGame(request, authToken);
    }
}
