package service;

import java.util.UUID;

import dataaccess.*;
import model.*;

public class RegisterService {

    public boolean userAlreadyInDatabase(String username) {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        return userDAO.getUser(username) != null;
    }

    public AuthData registerUser(UserData userData) {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        userDAO.createUser(userData);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.addAuth(authData);
        return authData;
    }
}
