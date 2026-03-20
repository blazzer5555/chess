package dataaccess;

import chess.ChessGame;
import model.GameData;
import server.JoinGameRequest;

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

    public void updateGame(JoinGameRequest request, String authToken) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        GameData gameToUpdate = gameIDToGameData.get(request.gameID());
        setOfGameData.remove(gameToUpdate);
        gameIDToGameData.remove(request.gameID());
        gameNameToGameData.remove(gameToUpdate.gameName());
        GameData updatedGame;
        if (Objects.equals(request.playerColor(), "WHITE")) {
            updatedGame = new GameData(gameToUpdate.gameID(), authDAO.getAuthByAuthToken(authToken).username(),
                    gameToUpdate.blackUsername(), gameToUpdate.gameName(),
                    gameToUpdate.game());
        }
        else {
            updatedGame = new GameData(gameToUpdate.gameID(), gameToUpdate.whiteUsername(),
                    authDAO.getAuthByAuthToken(authToken).username(),
                    gameToUpdate.gameName(), gameToUpdate.game());
        }
        updateDataStructures(updatedGame);
    }

    public void clear() {
        setOfGameData.clear();
        gameIDToGameData.clear();
        gameNameToGameData.clear();
        setOfUsedIDs.clear();
    }

    private void updateDataStructures(GameData updatedGame) {
        setOfGameData.add(updatedGame);
        gameIDToGameData.put(updatedGame.gameID(), updatedGame);
        gameNameToGameData.put(updatedGame.gameName(), updatedGame);
    }
}
