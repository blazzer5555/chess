package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import model.AuthData;

import java.sql.SQLException;

public class LogoutService {

    public boolean userIsLoggedIn(String authToken) throws SQLException, DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public void logOutUser(String authToken) throws SQLException, DataAccessException{
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        AuthData authData = authDAO.getAuthByAuthToken(authToken);
        authDAO.deleteAuth(authData);
    }

}
