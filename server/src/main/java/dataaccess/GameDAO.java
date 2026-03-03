package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;
import java.util.Random;

public class GameDAO {

    static Set<GameData> setOfGameData = new HashSet<>();
    static Set<Integer> setOfUsedIDs = new HashSet<>();
    static Map<Integer, GameData> gameIDToGameData = new HashMap<>();
    static Map<String, GameData> gameNameToGameData = new HashMap<>();

    public int createGame(String gameName) {
        boolean isUniqueID = false;
        Random rand = new Random();
        int ID = 0;
        while(!isUniqueID) {
            ID = rand.nextInt(0,1001);
            if (!setOfUsedIDs.contains(ID)) {
                isUniqueID = true;
            }
        }
        GameData newGame = new GameData(ID, null, null, gameName, new ChessGame());
        setOfGameData.add(newGame);
        gameIDToGameData.put(ID, newGame);
        gameNameToGameData.put(gameName, newGame);
        return ID;
    }

    public GameData getGameByID(int ID) {
        return gameIDToGameData.getOrDefault(ID, null);
    }

    public GameData getGameByName(String name) {
        return gameNameToGameData.getOrDefault(name, null);
    }

    public List<GameData> listGames() {
        return new ArrayList<>(setOfGameData);
    }

    public void updateGame() {

    }

    public void clear() {
        setOfGameData.clear();
        gameIDToGameData.clear();
        gameNameToGameData.clear();
        setOfUsedIDs.clear();
    }
}
