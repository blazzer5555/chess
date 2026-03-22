package dataaccess;

import model.AuthData;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseAuthDAO {

    public AuthData getAuthByAuthToken(String authToken) throws SQLException, DataAccessException{
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
        }
        return null;
    }

    public void addAuth(AuthData authData) throws SQLException, DataAccessException{
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
    }

    public void deleteAuth(AuthData authData) throws SQLException, DataAccessException{
        Gson gson = new Gson();
        String serializedAuthData = gson.toJson(authData);
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authdata WHERE authdata = ?")) {
                preparedStatement.setString(1, serializedAuthData);
                preparedStatement.executeUpdate();
            }
        }
    }

    public void clear() throws SQLException, DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authdata")) {
                preparedStatement.executeUpdate();
            }
        }
    }
}