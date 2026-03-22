package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import server.JoinGameRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DatabaseGameDAO {

    public int createGame(String gameName) throws SQLException, DataAccessException {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String createStatement = "INSERT INTO gamedata(gamedata, gamename) values (?, ?)";
            try (var preparedStatement1 = conn.prepareStatement(createStatement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement1.setString(1, "null");
                preparedStatement1.setString(2, gameName);
                preparedStatement1.executeUpdate();
                var resultSet = preparedStatement1.getGeneratedKeys();
                var ID = 0;
                if (resultSet.next()) {
                    ID = resultSet.getInt(1);
                    GameData gameData = new GameData(ID, null, null, gameName, new ChessGame());
                    String serializedGameData = gson.toJson(gameData);
                    String updateStatement = "UPDATE gamedata SET gamedata = ? WHERE id = ?";
                    try (var preparedStatement2 = conn.prepareStatement(updateStatement)) {
                        preparedStatement2.setString(1, serializedGameData);
                        preparedStatement2.setInt(2, ID);
                        preparedStatement2.executeUpdate();
                        return ID;
                    }
                }
            }
        }
        return -1;
    }

    public GameData getGameByID(int gameID) throws SQLException, DataAccessException{
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT gamedata FROM gamedata WHERE id = ?";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                preparedStatement.setInt(1, gameID);
                ResultSet rs = preparedStatement.executeQuery();
                var serializedGameData = "";
                if (rs.next()) {
                    serializedGameData = rs.getString(1);
                    return gson.fromJson(serializedGameData, GameData.class);
                }
            }
        }
        return null;
    }

    public GameData getGameByName(String name) throws SQLException, DataAccessException{
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT gamedata FROM gamedata WHERE gamename = ?";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                preparedStatement.setString(1, name);
                ResultSet rs = preparedStatement.executeQuery();
                var serializedGameData = "";
                if (rs.next()) {
                    serializedGameData = rs.getString(1);
                    return gson.fromJson(serializedGameData, GameData.class);
                }
            }
        }
        return null;
    }

    public List<GameData> listGames() throws SQLException, DataAccessException{
        List<GameData> gameList = new ArrayList<>();
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT gamedata FROM gamedata";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()) {
                    var serializedGameData = "";
                    serializedGameData = rs.getString(1);
                    gameList.add(gson.fromJson(serializedGameData, GameData.class));
                }
            }
        }
        return gameList;
    }

    public void joinPlayer(JoinGameRequest request, String authToken) throws SQLException, DataAccessException{
        Gson gson = new Gson();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT gamedata FROM gamedata WHERE id = ?";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                preparedStatement.setInt(1, request.gameID());
                ResultSet rs = preparedStatement.executeQuery();
                var oldSerializedGameData = "";
                if (rs.next()) {
                    oldSerializedGameData = rs.getString(1);
                    GameData oldGameData = gson.fromJson(oldSerializedGameData, GameData.class);
                    GameData newGameData;
                    String playerUsername = authDAO.getAuthByAuthToken(authToken).username();
                    if (Objects.equals(request.playerColor(), "WHITE")) {
                        newGameData = new GameData(oldGameData.gameID(), playerUsername, oldGameData.blackUsername(), oldGameData.gameName(), oldGameData.game());
                    }
                    else {
                        newGameData = new GameData(oldGameData.gameID(), oldGameData.whiteUsername(), playerUsername, oldGameData.gameName(), oldGameData.game());
                    }
                    var newSerializedGameData = gson.toJson(newGameData);
                    String updateStatement = "UPDATE gamedata SET gamedata = ? WHERE id = ?";
                    try (var preparedStatement2 = conn.prepareStatement(updateStatement)) {
                        preparedStatement2.setString(1, newSerializedGameData);
                        preparedStatement2.setInt(2, oldGameData.gameID());
                        preparedStatement2.executeUpdate();
                    }
                }
            }
        }
    }

    public void clear() throws SQLException, DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM gamedata")) {
                preparedStatement.executeUpdate();
            }
        }
    }
}