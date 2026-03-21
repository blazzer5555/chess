package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import java.sql.ResultSet;

public class DatabaseUserDAO {

    public UserData getUserByUsername(String username) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String getStatement1 = "SELECT userdata FROM userdata WHERE username = '" + username + "'";
            try (var preparedStatement = conn.prepareStatement(getStatement1)) {
                ResultSet rs = preparedStatement.executeQuery();
                var serializedUserData = "";
                if (rs.next()) {
                    serializedUserData = rs.getString(1);
                }
                return gson.fromJson(serializedUserData, UserData.class);
            }
            catch (Exception e) {
                return null;
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
            String createStatement = "INSERT INTO userdata(userdata, username) values ('" + serializedUserData + "','" + userData.username() + "')";
            try (var preparedStatement = conn.prepareStatement(createStatement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not add UserData to the database.");
        }
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            try (var preparedStatement = conn.prepareStatement("DELETE FROM userdata")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the user database.");
        }
    }
}
