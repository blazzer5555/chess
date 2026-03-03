package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthDAO {

    static Set<AuthData> SetOfAuthData = new HashSet<>();
    static Map<String, AuthData> authTokenToAuthData = new HashMap<>();
    static Map<String, AuthData> usernameToAuthData = new HashMap<>();

    public AuthData getAuthByAuthToken(String authToken) {
        return authTokenToAuthData.getOrDefault(authToken, null);
    }

    public AuthData getAuthByUsername(String username) {
        return usernameToAuthData.getOrDefault(username, null);
    }

    public void addAuth(AuthData authData) {
        SetOfAuthData.add(authData);
        authTokenToAuthData.put(authData.authToken(), authData);
        usernameToAuthData.put(authData.username(), authData);
    }

    public void clear() {
        SetOfAuthData.clear();
        authTokenToAuthData.clear();
        usernameToAuthData.clear();
    }
}
