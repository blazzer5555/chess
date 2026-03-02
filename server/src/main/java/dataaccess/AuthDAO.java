package dataaccess;

import model.*;
import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthDAO {

    static Set<AuthData> setOfAuthTokens = new HashSet<>();
    static Map<String, AuthData> authTokenToAuthData = new HashMap<>();

    public AuthData getAuth(String authToken) {
        return authTokenToAuthData.getOrDefault(authToken, null);
    }

    public void createAuth(AuthData authData) {
        setOfAuthTokens.add(authData);
        authTokenToAuthData.put(authData.authToken(), authData);
    }

    public void clear() {
        setOfAuthTokens.clear();
        authTokenToAuthData.clear();
    }
}
