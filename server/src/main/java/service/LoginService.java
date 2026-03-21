package service;

import dataaccess.*;
import model.*;
import server.LoginRequest;

import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public AuthData logInUser(UserData userData) {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.addAuth(authData);
        return authData;
    }

    public UserData getUser(LoginRequest loginRequest) {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        return userDAO.getUser(loginRequest.username());
    }

    public boolean isValidPassword(UserData userData, LoginRequest loginrequest) {
        return Objects.equals(userData.password(), loginrequest.password());
    }
}
