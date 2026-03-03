package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameDAO {

    Set<GameData> setOfGameData = new HashSet<>();
    Map<Integer, GameData> gameIDToGameData = new HashMap<>();

    public void createGame() {

    }

    public ChessGame getGameByID(int ID) {
        return null;
    }

    public List<GameData> listGames() {
        return new ArrayList<>(setOfGameData);
    }

    public void updateGame() {

    }

    public void clear() {
        setOfGameData.clear();
        gameIDToGameData.clear();
    }
}
