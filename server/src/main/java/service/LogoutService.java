package service;

import dataaccess.DatabaseAuthDAO;
import model.AuthData;

public class LogoutService {

    public boolean userIsLoggedIn(String authToken) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public void logOutUser(String authToken) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        AuthData authData = authDAO.getAuthByAuthToken(authToken);
        authDAO.deleteAuth(authData);
    }

}
