package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserDAO {

    public UserData getUserByUsername(String username) throws SQLException, DataAccessException{
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT userdata FROM userdata WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();
                var serializedUserData = "";
                if (rs.next()) {
                    serializedUserData = rs.getString(1);
                    return gson.fromJson(serializedUserData, UserData.class);
                }
            }
        }
        return null;
    }

    public void createUser(UserData userData) throws SQLException, DataAccessException{
        Gson gson = new Gson();
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        UserData encryptedUserData = new UserData(userData.username(), hashedPassword, userData.email());
        String serializedUserData = gson.toJson(encryptedUserData);
        try (var conn = DatabaseManager.getConnection()) {
            String createStatement = "INSERT INTO userdata(userdata, username) values (?, ?)";
            try (var preparedStatement = conn.prepareStatement(createStatement)) {
                preparedStatement.setString(1, serializedUserData);
                preparedStatement.setString(2, userData.username());
                preparedStatement.executeUpdate();
            }
        }
    }

    public void clear() throws SQLException, DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM userdata")) {
                preparedStatement.executeUpdate();
            }
        }
    }
}