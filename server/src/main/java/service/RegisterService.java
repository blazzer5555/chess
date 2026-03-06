package service;

import java.util.UUID;

import dataaccess.*;
import model.*;

public class RegisterService {

    public boolean userAlreadyInDatabase(String username) {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUser(username) != null;
    }

    public AuthData registerUser(UserData userData) {
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        userDAO.createUser(userData);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.addAuth(authData);
        return authData;
    }
}
