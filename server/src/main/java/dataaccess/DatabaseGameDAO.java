package dataaccess;

import chess.ChessGame;
import model.GameData;
import server.JoinGameRequest;

import java.util.*;

public class DatabaseGameDAO {

    private static final String[] deletionStatements = {
            "DELETE FROM gamename",
            "DELETE FROM gamedata"
        };

    public int createGame(String gameName) {
        return 0;
    }

    public GameData getGameByID(int gameID) {
        return null;
    }

    public GameData getGameByName(String name) {
        return null;
    }

    public List<GameData> listGames() {
        return null;
    }

    public void updateGame(JoinGameRequest request, String authToken) {
        return;
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chessdatabase");
            for (String statement: deletionStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Could not clear the game database.");
        }
    }

    private void updateDataStructures(GameData updatedGame) {
        return;
    }
}
