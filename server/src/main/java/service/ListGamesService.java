package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.List;

public class ListGamesService {

    public boolean userIsLoggedIn(String authToken) throws SQLException, DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public List<GameData> listCurrentGames() throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        return gameDAO.listGames();
    }
}
