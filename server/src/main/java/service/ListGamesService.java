package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;

import java.util.List;

public class ListGamesService {

    public boolean isUserLoggedIn(String authToken) {
        AuthDAO authDAO = new AuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public List<GameData> listCurrentGames() {
        GameDAO gameDAO = new GameDAO();
        return gameDAO.listGames();
    }
}
