package dataaccess;

import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UserDAO {

    static Set<UserData> setOfUsers = new HashSet<>();
    static Map<String, UserData> usernameToUserData = new HashMap<>();

    public UserData getUser(String username) {
        return usernameToUserData.getOrDefault(username, null);
    }

    public void createUser(UserData userData) {
        setOfUsers.add(userData);
        usernameToUserData.put(userData.username(), userData);
    }

    public void clear() {
        setOfUsers.clear();
        usernameToUserData.clear();
    }
}
