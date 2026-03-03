package service;

import dataaccess.*;
import model.*;
import server.LoginRequest;

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public AuthData logInUser(UserData userData) {
        AuthDAO authDAO = new AuthDAO();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.addAuth(authData);
        return authData;
    }

    public UserData getUser(LoginRequest loginRequest) {
        UserDAO userDAO = new UserDAO();
        return userDAO.getUser(loginRequest.username());
    }

    public boolean isValidPassword(UserData userData, LoginRequest loginrequest) {
        return Objects.equals(userData.password(), loginrequest.password());
    }

    public boolean isAlreadyLoggedIn(UserData userData) {
        AuthDAO authDAO = new AuthDAO();
        return authDAO.getAuthByUsername(userData.username()) != null;
    }
}
