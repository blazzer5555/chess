package dataaccess;

import model.AuthData;
import com.google.gson.Gson;
import server.LoginRequest;

public class DatabaseAuthDAO {

    private static final String[] clearStatements = {
            "DELETE FROM authtoken",
            "DELETE FROM authdata"
        };

    public AuthData getAuthByAuthToken(String authToken) {
        return null;
    }

    public void addAuth(AuthData authData) {
        Gson gson = new Gson();
        String serializedAuthData = gson.toJson(authData);
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String[] insertStatements = {
                    "INSERT INTO authdata(authdata) values (" + serializedAuthData + ");",
                    "INSERT INTO authtoken(authtoken) values (" + authData.authToken() + ");"
            };
            for (String statement: insertStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not add AuthData to the database.");
        }
    }

    public void deleteAuth(AuthData authData) {
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String[] deleteStatements = {};
            for (String statement: deleteStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not delete AuthData from the database.");
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
