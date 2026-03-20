package dataaccess;

import model.*;

import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO {

    static Map<String, UserData> usernameToUserData = new HashMap<>();

    public UserData getUser(String username) {
        return usernameToUserData.getOrDefault(username, null);
    }

    public void createUser(UserData userData) {
        usernameToUserData.put(userData.username(), userData);
    }

    public void clear() {
        usernameToUserData.clear();
    }
}
