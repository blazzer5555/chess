package service;

import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;

public class CreateGameService {

    public boolean userIsLoggedIn(String authToken) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameNameIsTaken(String gameName) {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        return gameDAO.getGameByName(gameName) != null;
    }

    public int createGame(String gameName) {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        return gameDAO.createGame(gameName);
    }
}
