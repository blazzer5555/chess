package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthDAO {

    static Set<AuthData> SetOfAuthData = new HashSet<>();
    static Map<String, AuthData> authTokenToAuthData = new HashMap<>();

    public AuthData getAuth(String authToken) {
        return authTokenToAuthData.getOrDefault(authToken, null);
    }

    public void createAuth(AuthData authData) {
        SetOfAuthData.add(authData);
        authTokenToAuthData.put(authData.authToken(), authData);
    }

    public void clear() {
        SetOfAuthData.clear();
        authTokenToAuthData.clear();
    }
}
