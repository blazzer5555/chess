package service;

import java.sql.SQLException;
import java.util.UUID;

import dataaccess.*;
import model.*;

public class RegisterService {

    public boolean userAlreadyInDatabase(String username) throws SQLException, DataAccessException{
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        return userDAO.getUserByUsername(username) != null;
    }

    public AuthData registerUser(UserData userData) throws SQLException, DataAccessException{
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        userDAO.createUser(userData);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.addAuth(authData);
        return authData;
    }
}
