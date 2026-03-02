package service;

import dataaccess.*;

public class ClearService {

    public void clearDatabase() {
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
