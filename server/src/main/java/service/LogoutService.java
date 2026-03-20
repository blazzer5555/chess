package service;

import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class LogoutService {

    public boolean userIsLoggedIn(String authToken) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.getAuthByAuthToken(authToken) != null;
    }

    public void logOutUser(String authToken) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        AuthData authData = authDAO.getAuthByAuthToken(authToken);
        authDAO.deleteAuth(authData);
    }

}
