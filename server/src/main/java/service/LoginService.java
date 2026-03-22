package service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import server.LoginRequest;

import java.sql.SQLException;
import java.util.UUID;

public class LoginService {

    public AuthData logInUser(UserData userData) throws SQLException, DataAccessException{
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authDAO.addAuth(authData);
        return authData;
    }

    public UserData getUser(LoginRequest loginRequest) throws SQLException, DataAccessException{
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        return userDAO.getUserByUsername(loginRequest.username());
    }

    public boolean isValidPassword(UserData userData, LoginRequest loginrequest) {
        return BCrypt.checkpw(loginrequest.password(), userData.password());
    }
}
