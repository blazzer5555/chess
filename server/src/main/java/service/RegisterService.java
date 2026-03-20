package service;

import java.util.UUID;

import dataaccess.*;
import model.*;

public class RegisterService {

    public boolean userAlreadyInDatabase(String username) {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        return userDAO.getUser(username) != null;
    }

    public AuthData registerUser(UserData userData) {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        userDAO.createUser(userData);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.addAuth(authData);
        return authData;
    }
}
