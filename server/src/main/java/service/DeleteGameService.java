package service;

import dataaccess.*;

import java.sql.SQLException;

public class DeleteGameService {

    public void removeGame(int gameID) throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.deleteGame(gameID);
    }

    public boolean userIsLoggedIn(String authToken) throws SQLException, DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameExists(int gameID) throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        return gameDAO.getGameByID(gameID) != null;
    }
}
