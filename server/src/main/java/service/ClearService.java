package service;

import dataaccess.*;

public class ClearService {

    public void clearDatabase() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
