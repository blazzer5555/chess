package dataaccess;

import model.AuthData;
import com.google.gson.Gson;
import model.UserData;
import server.LoginRequest;

import java.sql.ResultSet;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DatabaseAuthDAO {

    private static final String[] clearStatements = {
            "DELETE FROM authtoken",
            "DELETE FROM authdata"
        };

    public AuthData getAuthByAuthToken(String authToken) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String getStatement1 = "SELECT authid FROM authtoken WHERE authtoken = \"" + authToken + "\"";
            try (var preparedStatement = conn.prepareStatement(getStatement1)) {
                ResultSet rs = preparedStatement.executeQuery();
                try {
                    int id = rs.getInt(1);
                    String getStatement2 = "SELECT authdata FROM authdata WHERE id = \"" + id + "\"";
                    try (var preparedStatement2 = conn.prepareStatement(getStatement2)) {
                        String serializedUserData = String.valueOf(preparedStatement2.executeQuery());
                        return gson.fromJson(serializedUserData, AuthData.class);
                    }
                }
                catch (Exception e) {
                    return null;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not get AuthData from the database.");
        }
        return null;
    }

    public void addAuth(AuthData authData) {
        Gson gson = new Gson();
        String serializedAuthData = gson.toJson(authData);
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            String createStatement1 = "INSERT INTO authdata(authdata) values ('" + serializedAuthData + "')";
            try (var preparedStatement1 = conn.prepareStatement(createStatement1, RETURN_GENERATED_KEYS)) {
                preparedStatement1.executeUpdate();
                ResultSet rs = preparedStatement1.getGeneratedKeys();
                var id = 0;
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                String createStatement2 = "INSERT INTO authtoken(authtoken, authid) values ('" + authData.authToken() +"','" + id + "')";
                try (var preparedStatement2 = conn.prepareStatement(createStatement2)) {
                    preparedStatement2.executeUpdate();
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
            System.out.println("Could not clear the auth database.");
        }
    }
}
