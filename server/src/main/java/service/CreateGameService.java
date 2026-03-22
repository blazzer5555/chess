package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;

import java.sql.SQLException;

public class CreateGameService {

    public boolean userIsLoggedIn(String authToken) throws SQLException, DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameNameIsTaken(String gameName) throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        return gameDAO.getGameByName(gameName) != null;
    }

    public int createGame(String gameName) throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        return gameDAO.createGame(gameName);
    }
}
