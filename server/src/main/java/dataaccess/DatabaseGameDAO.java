package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import server.JoinGameRequest;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class DatabaseGameDAO {

    public int createGame(String gameName) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String createStatement1 = "INSERT INTO gamedata(gamedata, gamename) values ('" + null + "','" + gameName + "')";
            try (var preparedStatement1 = conn.prepareStatement(createStatement1, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement1.executeUpdate();
                var resultSet = preparedStatement1.getGeneratedKeys();
                var ID = 0;
                if (resultSet.next()) {
                    ID = resultSet.getInt(1);
                    GameData gameData = new GameData(ID, null, null, gameName, new ChessGame());
                    String serializedGameData = gson.toJson(gameData);
                    String createStatement2 = "UPDATE gamedata SET gamedata = '" + serializedGameData + "' WHERE id = " + ID;
                    try (var preparedStatement2 = conn.prepareStatement(createStatement2)) {
                        preparedStatement2.executeUpdate();
                        return ID;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not add AuthData to the database.");
        }
        return -1;
    }

    public GameData getGameByID(int gameID) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT gamedata FROM gamedata WHERE id = '" + gameID + "'";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                ResultSet rs = preparedStatement.executeQuery();
                var serializedGameData = "";
                if (rs.next()) {
                    serializedGameData = rs.getString(1);
                    return gson.fromJson(serializedGameData, GameData.class);
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

    public GameData getGameByName(String name) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT gamedata FROM gamedata WHERE gamename = '" + name + "'";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
                ResultSet rs = preparedStatement.executeQuery();
                var serializedGameData = "";
                if (rs.next()) {
                    serializedGameData = rs.getString(1);
                    return gson.fromJson(serializedGameData, GameData.class);
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

    public List<GameData> listGames() {
        List<GameData> gameList = new ArrayList<>();
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement1 = "SELECT gamedata FROM gamedata";
            try (var preparedStatement = conn.prepareStatement(getStatement1)) {
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()) {
                    var serializedGameData = "";
                    serializedGameData = rs.getString(1);
                    gameList.add(gson.fromJson(serializedGameData, GameData.class));
                }
                return gameList;
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

    public void joinPlayer(JoinGameRequest request, String authToken) {
        Gson gson = new Gson();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "SELECT gamedata FROM gamedata WHERE id = '" + request.gameID() + "'";
            try (var preparedStatement = conn.prepareStatement(getStatement)) {
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
                    String updateStatement = "UPDATE gamedata SET gamedata = '" + newSerializedGameData + "' WHERE id = " + oldGameData.gameID();
                    try (var preparedStatement2 = conn.prepareStatement(updateStatement)) {
                        preparedStatement2.executeUpdate();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Could not updateGameData from the database.");
            }
        }
        catch (Exception e) {
            System.out.println("Could not update GameData to the database.");
        }
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM gamedata")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the game database.");
        }
    }
}
