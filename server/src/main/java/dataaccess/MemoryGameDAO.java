
package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;
import java.util.Random;

public class MemoryGameDAO {

    static Set<GameData> setOfGameData = new HashSet<>();
    static Set<Integer> setOfUsedIDs = new HashSet<>();
    static Map<Integer, GameData> gameIDToGameData = new HashMap<>();
    static Map<String, GameData> gameNameToGameData = new HashMap<>();

    public int createGame(String gameName) {
        boolean isUniqueID = false;
        Random rand = new Random();
        int gameID = 0;
        while(!isUniqueID) {
            gameID = rand.nextInt(0,1001);
            if (!setOfUsedIDs.contains(gameID)) {
                isUniqueID = true;
            }
        }
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        setOfGameData.add(newGame);
        gameIDToGameData.put(gameID, newGame);
        gameNameToGameData.put(gameName, newGame);
        return gameID;
    }

    public GameData getGameByID(int gameID) {
        return gameIDToGameData.getOrDefault(gameID, null);
    }

    public GameData getGameByName(String name) {
        return gameNameToGameData.getOrDefault(name, null);
    }

    public List<GameData> listGames() {
        return new ArrayList<>(setOfGameData);
    }

    public void clear() {
        setOfGameData.clear();
        gameIDToGameData.clear();
        gameNameToGameData.clear();
        setOfUsedIDs.clear();
    }
}
