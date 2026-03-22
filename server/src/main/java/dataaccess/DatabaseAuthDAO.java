package dataaccess;

import model.AuthData;
import com.google.gson.Gson;
import java.sql.ResultSet;

public class DatabaseAuthDAO {

    public AuthData getAuthByAuthToken(String authToken) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT authdata FROM authdata WHERE authtoken = ?";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                preparedStatement.setString(1, authToken);
                ResultSet rs = preparedStatement.executeQuery();
                var serializedAuthData = "";
                if (rs.next()) {
                    serializedAuthData = rs.getString(1);
                    return gson.fromJson(serializedAuthData, AuthData.class);
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

    public void addAuth(AuthData authData) {
        Gson gson = new Gson();
        String serializedAuthData = gson.toJson(authData);
        try (var conn = DatabaseManager.getConnection()) {
            String createStatement = "INSERT INTO authdata(authdata, authtoken) values (?, ?)";
            try (var preparedStatement = conn.prepareStatement(createStatement)) {
                preparedStatement.setString(1, serializedAuthData);
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not add AuthData to the database.");
        }
    }

    public void deleteAuth(AuthData authData) {
        Gson gson = new Gson();
        String serializedAuthData = gson.toJson(authData);
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authdata WHERE authdata = ?")) {
                preparedStatement.setString(1, serializedAuthData);
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not delete AuthData from the database.");
        }
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authdata")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the auth database.");
        }
    }
}
