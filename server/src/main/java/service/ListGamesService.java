package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;

import java.util.List;

public class ListGamesService {

    public boolean userIsLoggedIn(String authToken) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public List<GameData> listCurrentGames() {
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        return gameDAO.listGames();
    }
}
