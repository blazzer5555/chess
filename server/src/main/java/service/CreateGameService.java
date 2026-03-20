package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;

public class CreateGameService {

    public boolean userIsLoggedIn(String authToken) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameNameIsTaken(String gameName) {
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        return gameDAO.getGameByName(gameName) != null;
    }

    public int createGame(String gameName) {
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        return gameDAO.createGame(gameName);
    }
}
