package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class DatabaseAuthDAO {

    private static final String[] deletionStatements = {
            "DELETE FROM authtoken",
            "DELETE FROM authdata"
        };

    public AuthData getAuthByAuthToken(String authToken) {
        return null;
    }

    public void addAuth(AuthData authData) {
    }

    public void deleteAuth(AuthData authData) {
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement: deletionStatements) {
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
