package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;

public class DatabaseUserDAO {

    public UserData getUserByUsername(String username) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement1 = "SELECT userdata FROM userdata WHERE username = '" + username + "'";
            try (var preparedStatement = conn.prepareStatement(getStatement1)) {
                ResultSet rs = preparedStatement.executeQuery();
                var serializedUserData = "";
                if (rs.next()) {
                    serializedUserData = rs.getString(1);
                    return gson.fromJson(serializedUserData, UserData.class);
                }
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
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        UserData encryptedUserData = new UserData(userData.username(), hashedPassword, userData.email());
        String serializedUserData = gson.toJson(encryptedUserData);
        try (var conn = DatabaseManager.getConnection()) {
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
            try (var preparedStatement = conn.prepareStatement("DELETE FROM userdata")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the user database.");
        }
    }
}
