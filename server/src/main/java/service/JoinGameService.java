package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import server.JoinGameRequest;

import java.sql.SQLException;
import java.util.Objects;

public class JoinGameService {

    public boolean userIsLoggedIn(String authToken) throws SQLException, DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public boolean gameExists(int gameID) throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
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

    public boolean colorAvailable(String color, int gameID) throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        if (Objects.equals(color, "WHITE")) {
            return gameDAO.getGameByID(gameID).whiteUsername() == null;
        }
        else {
            return gameDAO.getGameByID(gameID).blackUsername() == null;
        }
    }

    public void joinGame(JoinGameRequest request, String authToken) throws SQLException, DataAccessException{
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.joinPlayer(request, authToken);
    }
}
