package service;

import dataaccess.*;

public class ClearService {

    public void clearDatabase() {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
