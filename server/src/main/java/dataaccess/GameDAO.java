package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameDAO {

    Set<GameData> setOfGameData = new HashSet<>();
    Map<String, GameData> gameNameToGameData = new HashMap<>();
    Map<Integer, GameData> gameIDToGameData = new HashMap<>();

    public void createGame() {

    }

    public ChessGame getGameByName(String name) {
        return null;
    }

    public ChessGame getGameByID(int ID) {
        return null;
    }

    public List<ChessGame> listGames() {
        return null;
    }

    public void updateGame() {

    }

    public void clear() {
        setOfGameData.clear();
        gameIDToGameData.clear();
        gameNameToGameData.clear();
    }
}
