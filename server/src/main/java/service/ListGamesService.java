package service;

import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import model.GameData;

import java.util.List;

public class ListGamesService {

    public boolean userIsLoggedIn(String authToken) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public List<GameData> listCurrentGames() {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        return gameDAO.listGames();
    }
}
