package service;

import java.util.UUID;

import dataaccess.*;
import model.*;

public class RegisterService {

    public AuthData registerUser(UserData userData) throws DataAccessException{
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        if (userDAO.getUser(userData.username()) != null) {
            throw new DataAccessException("");
        }
        else {
            userDAO.createUser(userData);
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, userData.username());
            authDAO.addAuth(authData);
            return authData;
        }
    }
}
