package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;

public class CreateGameService {

    public boolean userIsLoggedIn(String authToken) {
        AuthDAO authDAO = new AuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameNameIsTaken(String gameName) {
        GameDAO gameDAO = new GameDAO();
        return gameDAO.getGameByName(gameName) != null;
    }

    public int createGame(String gameName) {
        GameDAO gameDAO = new GameDAO();
        return gameDAO.createGame(gameName);
    }
}
