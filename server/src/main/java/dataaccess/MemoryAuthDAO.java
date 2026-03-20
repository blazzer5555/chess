package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO {

    static Map<String, AuthData> authTokenToAuthData = new HashMap<>();
    static Map<String, AuthData> usernameToAuthData = new HashMap<>();

    public AuthData getAuthByAuthToken(String authToken) {
        return authTokenToAuthData.getOrDefault(authToken, null);
    }

    public void addAuth(AuthData authData) {
        authTokenToAuthData.put(authData.authToken(), authData);
        usernameToAuthData.put(authData.username(), authData);
    }

    public void deleteAuth(AuthData authData) {
        authTokenToAuthData.remove(authData.authToken());
        usernameToAuthData.remove(authData.username());
    }

    public void clear() {
        authTokenToAuthData.clear();
        usernameToAuthData.clear();
    }
}
