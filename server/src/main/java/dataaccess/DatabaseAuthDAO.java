package dataaccess;

import model.AuthData;
import com.google.gson.Gson;
import model.UserData;
import java.sql.ResultSet;

public class DatabaseAuthDAO {

    private static final String[] clearStatements = {
            "DELETE FROM authtoken",
            "DELETE FROM authdata"
        };

    public AuthData getAuthByAuthToken(String authToken) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String getStatement1 = "SELECT authdata FROM authdata WHERE authtoken = '" + authToken + "'";
            try (var preparedStatement = conn.prepareStatement(getStatement1)) {
                ResultSet rs = preparedStatement.executeQuery();
                var serializedAuthData = "";
                if (rs.next()) {
                    serializedAuthData = rs.getString(1);
                }
                return gson.fromJson(serializedAuthData, AuthData.class);
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
            conn.setCatalog("chessdatabase");
            String createStatement1 = "INSERT INTO authdata(authdata, authtoken) values ('" + serializedAuthData + "','" + authData.authToken() + "')";
            try (var preparedStatement1 = conn.prepareStatement(createStatement1)) {
                preparedStatement1.executeUpdate();
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
            conn.setCatalog("chessdatabase");
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authdata WHERE authdata = '" + serializedAuthData + "'")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not delete AuthData from the database.");
        }
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authdata")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the auth database.");
        }
    }
}
