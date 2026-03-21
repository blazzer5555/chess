package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DatabaseUserDAO {

    private static final String[] clearStatements = {
            "DELETE FROM username",
            "DELETE FROM userdata"
        };

    public UserData getUserByUsername(String username) {
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
            String createStatement1 = "INSERT INTO userdata(userdata) values ('" + serializedUserData + "')";
            try (var preparedStatement1 = conn.prepareStatement(createStatement1, RETURN_GENERATED_KEYS)) {
                preparedStatement1.executeUpdate();
                ResultSet rs = preparedStatement1.getGeneratedKeys();
                var id = 0;
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                String createStatement2 = "INSERT INTO username(username, userid) values ('" + userData.username() +"','" + id + "')";
                try (var preparedStatement2 = conn.prepareStatement(createStatement2)) {
                    preparedStatement2.executeUpdate();
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
            System.out.println("Could not clear the user database.");
        }
    }
}
