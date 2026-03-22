package service;

import dataaccess.*;

import java.sql.SQLException;

public class ClearService {

    public void clearDatabase() throws SQLException, DataAccessException{
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
