package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class DatabaseUserDAO {

    private static final String[] deletionStatements = {
            "DELETE FROM username",
            "DELETE FROM userdata"
        };

    public UserData getUser(String username) {
        return null;
    }

    public void createUser(UserData userData) {
        return;
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
