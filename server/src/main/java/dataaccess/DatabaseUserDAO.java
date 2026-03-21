package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;

public class DatabaseUserDAO {

    private static final String[] clearStatements = {
            "DELETE FROM username",
            "DELETE FROM userdata"
        };

    public UserData getUser(String username) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String getStatement1 = "SELECT userid FROM username WHERE username = \"" + username + "\"";
            try (var preparedStatement = conn.prepareStatement(getStatement1)) {
                ResultSet rs = preparedStatement.executeQuery();
                try {
                    int id = rs.getInt(1);
                    String getStatement2 = "SELECT userdata FROM userdata WHERE id = \"" + id + "\"";
                    try (var preparedStatement2 = conn.prepareStatement(getStatement2)) {
                        String serializedUserData = String.valueOf(preparedStatement2.executeQuery());
                        return gson.fromJson(serializedUserData, UserData.class);
                    }
                }
                catch (Exception e) {
                    return null;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not get UserData from the database.");
        }
        return null;
    }

    public void createUser(UserData userData) {
        Gson gson = new Gson();
        String serializedUserData = gson.toJson(userData);
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String[] createStatements = {
                    "INSERT INTO userdata(userdata) values (" + serializedUserData + ");",
                    "INSERT INTO username(username) values (" + userData.username() + ");"
            };
            for (String statement: createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not add UserData to the database.");
        }
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            for (String statement: clearStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the database.");
        }
    }
}
