package dataaccess;

import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UserDAO {

    Set<UserData> setOfUsers = new HashSet<>();
    Map<String, UserData> usernameToUserData = new HashMap<>();

    UserData getUser(String username) {
        return usernameToUserData.getOrDefault(username, null);
    }

    void createUser(UserData userData) {
        setOfUsers.add(userData);
        usernameToUserData.put(userData.username(), userData);
    }

    void clear() {
        setOfUsers.clear();
        usernameToUserData.clear();
    }
}
