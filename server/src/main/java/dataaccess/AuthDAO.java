package dataaccess;

import model.*;
import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthDAO {

    Set<AuthData> setOfAuthTokens = new HashSet<>();
    Map<String, AuthData> authTokenToAuthData = new HashMap<>();

    AuthData getAuth(String authToken) {
        return authTokenToAuthData.getOrDefault(authToken, null);
    }

    void createAuth(AuthData authData) {
        setOfAuthTokens.add(authData);
        authTokenToAuthData.put(authData.authToken(), authData);
    }

    void clear() {
        setOfAuthTokens.clear();
        authTokenToAuthData.clear();
    }
}
