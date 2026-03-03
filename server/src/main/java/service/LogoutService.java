package service;

import dataaccess.AuthDAO;
import model.AuthData;

public class LogoutService {

    public boolean userIsLoggedIn(String authToken) {
        AuthDAO authDAO = new AuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public void logOutUser(String authToken) {
        AuthDAO authDAO = new AuthDAO();
        AuthData authData = authDAO.getAuthByAuthToken(authToken);
        authDAO.deleteAuth(authData);
    }

}
