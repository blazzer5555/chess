package dataaccess;

import model.GameData;
import server.JoinGameRequest;

import java.util.*;

public class DatabaseGameDAO {

    public int createGame(String gameName) {
        return 0;
        // create a new GameData object and add it to the database
    }

    public GameData getGameByID(int gameID) {
        return null;
        // this is not the primary key id, this is the id shown when the user asks for available games in the REPL
    }

    public GameData getGameByName(String name) {
        return null;
        // find game by gamename in database, then return GameData
    }

    public List<GameData> listGames() {
        return null;
        // query for all items in column gamedata in table gamedata
    }

    public void joinPlayer(JoinGameRequest request, String authToken) {
        return;
        // query for the game using data from the request object, recieve it, modify it,
        // then save it back into the databse under the same id name (USE RETURN_GENERATED_KEYS)
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            try (var preparedStatement = conn.prepareStatement("DELETE FROM gamedata")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the game database.");
        }
    }
}
