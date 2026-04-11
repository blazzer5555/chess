package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.*;
import websocket.commands.UserGameCommand;

import java.util.*;

//Make a move on the chess board in the form of \"[Position of piece you want to move] " +
//                        "[Location where you want it to move]\". For example, \"e2 e4\"");

public class ClientLoopService {

    Map<Integer, Integer> mapOfIDs = new HashMap<>();
    int maxIDNumber = 1;
    final Scanner SCANNER = new Scanner(System.in);
    final ServerFacade SERVER = new ServerFacade();
    boolean mapOfIDsPopulated = false;

    public void runLoop() {
        System.out.println("Did someone ask to play chess?");
        boolean doneWithProgram = false;
        int currentGameID = 0;
        String authToken = null;
        while (!doneWithProgram) {
            if (authToken != null) {
                if (currentGameID == 0) {
                    LoginLoopData loginLoopData = loginLoop(authToken);
                    authToken = loginLoopData.authToken();
                    currentGameID = loginLoopData.currentGameID();
                }
                else {
                    currentGameID = gameplayLoop(authToken, currentGameID);
                }
            }
            else {
                PreLoginLoopData userStatuses = preLoginLoop();
                doneWithProgram = userStatuses.doneWithProgram();
                authToken = userStatuses.authToken();
            }
        }
        System.out.println("Thanks for playing!");
    }
    private PreLoginLoopData preLoginLoop() {
        boolean doneWithProgram = false;
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Quit\n3. Log in\n4. Register");
        int userResponse;
        try {
            userResponse = Integer.parseInt(SCANNER.nextLine());
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
            return new PreLoginLoopData(false, null);
        }
        switch (userResponse) {
            case (1):
                System.out.println("Quit: Exit the program.");
                System.out.println("Log in: Log in to an existing chess account.");
                System.out.println("Register: Register a new chess account.");
                break;
            case (2):
                doneWithProgram = true;
                break;
            case (3):
                return login();
            case (4):
                return register();
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return new PreLoginLoopData(doneWithProgram, null);
    }

    private LoginLoopData loginLoop(String authToken) {
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Log out\n3. Create game\n4. List games\n5. Play game\n6. Spectate game");
        int userResponse;
        try {
            userResponse = Integer.parseInt(SCANNER.nextLine());
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
            return new LoginLoopData(authToken, 0);
        }
        switch (userResponse) {
            case (1):
                System.out.println("Log out: Log out of the program and go back to the home screen.");
                System.out.println("Create game: Create a new chess game. You will have to join the game separately.");
                System.out.println("List games: List all the games currently available, along with their IDs, and who's playing which color.");
                System.out.println("Play game: Join a game as either the white or black player.");
                System.out.println("Spectate game: Observe a game that is being played by other players.");
                break;
            case(2):
                return logout(authToken);
            case(3):
                createGame(authToken);
                break;
            case(4):
                listGames(authToken);
                break;
            case(5):
                return joinGame(authToken);
            case(6):
                return spectateGame(authToken);
            case(41):
                deleteGame(authToken);
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return new LoginLoopData(authToken, 0);
    }

    private int gameplayLoop(String authToken, int gameID) {
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Leave game\n3. Redraw chess board\n4. Make move\n5. Resign\n6. Highlight legal moves");
        int userResponse;
        try {
            userResponse = Integer.parseInt(SCANNER.nextLine());
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
            return gameID;
        }
        switch (userResponse) {
            case (1):
                System.out.println("Leave game: This will make you leave the game, but players can join the open spot, including you.");
                System.out.println("Redraw chess board: Redraws the chess board so you don't have to keep scrolling back up to see it.");
                System.out.println("Make move: If it's your turn, make a move on the board.");
                System.out.println("Resign: Forfeit the game. This results in you losing and nobody else being able to makes further moves.");
                System.out.println("Highlight legal moves: Given a place on the board, highlight all the legal moves that piece can make.");
                break;
            case(2):
                return leaveGame(authToken, gameID);
            case(3):
                redrawBoard(authToken, gameID);
                break;
            case(4):
                makeMove(authToken, gameID);
                break;
            case(5):
                resign(authToken, gameID);
                break;
            case(6):
                highlightLegalMoves(authToken, gameID);
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return gameID;
    }

    private void highlightLegalMoves(String authToken, int gameID) {
        System.out.println("Please enter the location for the piece you want to check. It should be in the format similar to \"e2\".\n");
        String userLocation = SCANNER.nextLine();
        ChessMove move = parseInputForLocation(userLocation);
        if (move == null) {
            return;
        }
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.HIGHLIGHT_BOARD, authToken, mapOfIDs.get(gameID), move);
        try {
            SERVER.sendWebsocketRequest(command);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the websocket connection.");
        }
    }

    private void resign(String authToken, int gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, mapOfIDs.get(gameID), null);
        try {
            SERVER.sendWebsocketRequest(command);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the websocket connection.");
        }
    }

    private void makeMove(String authToken, int gameID) {
        System.out.println("Please enter your move. It should be in the format similar to \"e2 f4\".");
        System.out.println("The first position is location of the piece you want to move. The second location is where you want to move it.");
        System.out.println("If this move would promote a pawn, add a space and the letter corresponding to the piece you want to promote " +
                "(n for knight, q for queen etc.).");
        String userMove = SCANNER.nextLine();
        ChessMove move = parseInputForMove(userMove);
        if (move == null) {
            return;
        }
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, mapOfIDs.get(gameID), move);
        try {
            SERVER.sendWebsocketRequest(command);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the websocket connection.");
        }
    }

    private ChessMove parseInputForLocation(String userLocation) {
        if (userLocation.length() < 2) {
            System.out.println("That is not a valid format. Please try again and read the instructions.");
            return null;
        }
        char userFirstCol = userLocation.charAt(0);
        char userFirstRow = userLocation.charAt(1);
        int firstColIndex = convertColToInt(userFirstCol);
        if (firstColIndex == 9) {
            System.out.println("Your location's letter is not a letter on the board.");
            return null;
        }
        int firstRowIndex = convertRowToInt(userFirstRow);
        if (firstRowIndex == 9) {
            System.out.println("Your location's number is not a number on the board.");
            return null;
        }
        ChessPosition position = new ChessPosition(firstRowIndex, firstColIndex);
        return new ChessMove(position, null, null);
    }

    private ChessMove parseInputForMove(String userMove) {
        if (userMove.length() < 5) {
            System.out.println("That is not a valid format. Please try again and read the instructions.");
            return null;
        }
        char userFirstCol = userMove.charAt(0);
        char userFirstRow = userMove.charAt(1);
        char userSecondCol = userMove.charAt(3);
        char userSecondRow = userMove.charAt(4);
        ChessPiece.PieceType pieceType = null;
        if (userMove.length() >= 7) {
            char pieceLetter = userMove.charAt(6);
            pieceType = switch(pieceLetter) {
                case ('q') -> ChessPiece.PieceType.QUEEN;
                case ('n') -> ChessPiece.PieceType.KNIGHT;
                case ('b') -> ChessPiece.PieceType.BISHOP;
                case ('r') -> ChessPiece.PieceType.ROOK;
                default -> ChessPiece.PieceType.KING;
            };
            if (pieceType == ChessPiece.PieceType.KING) {
                System.out.println("That is not a valid letter for a piece to promote to. " +
                        "'q' is for queen, 'n' is for knight, 'b' is for bishop, and 'r' is for rook.");
                return null;
            }
        }
        int firstColIndex = convertColToInt(userFirstCol);
        if (firstColIndex == 9) {
            System.out.println("Your first location's letter is not a letter on the board.");
            return null;
        }
        int secondColIndex = convertColToInt(userSecondCol);
        if (secondColIndex == 9) {
            System.out.println("Your second location's letter is not a letter on the board.");
            return null;
        }
        int firstRowIndex = convertRowToInt(userFirstRow);
        if (firstRowIndex == 9) {
            System.out.println("Your first location's number is not a number on the board.");
            return null;
        }
        int secondRowIndex = convertRowToInt(userSecondRow);
        if (secondRowIndex == 9) {
            System.out.println("Your second location's number is not a number on the board.");
            return null;
        }
        ChessPosition startPosition = new ChessPosition(firstRowIndex, firstColIndex);
        ChessPosition endPosition = new ChessPosition(secondRowIndex, secondColIndex);
        return new ChessMove(startPosition, endPosition, pieceType);
    }

    private int convertRowToInt(char userRow) {
        return switch (userRow) {
            case ('1') -> 1;
            case ('2') -> 2;
            case ('3') -> 3;
            case ('4') -> 4;
            case ('5') -> 5;
            case ('6') -> 6;
            case ('7') -> 7;
            case ('8') -> 8;
            default -> 9;
        };
    }

    private int convertColToInt(char userCol) {
        return switch (userCol) {
            case ('a') -> 1;
            case ('b') -> 2;
            case ('c') -> 3;
            case ('d') -> 4;
            case ('e') -> 5;
            case ('f') -> 6;
            case ('g') -> 7;
            case ('h') -> 8;
            default -> 9;
        };
    }

    private int leaveGame(String authToken, int gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, mapOfIDs.get(gameID), null);
        try {
            SERVER.sendWebsocketRequest(command);
            System.out.println("You have left the game.");
            return 0;
        }
        catch (Exception e) {
            System.out.println("Something went wrong trying to leave the game. Please try again later.");
            return gameID;
        }
    }

    private void redrawBoard(String authToken, int gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.GET_BOARD, authToken, mapOfIDs.get(gameID), null);
        try {
            SERVER.sendWebsocketRequest(command);
        }
        catch (Exception e) {
            System.out.println("Something went wrong trying to leave the game. Please try again later.");
        }
    }

    private PreLoginLoopData register() {
        System.out.println("Please enter a username.");
        String registerUsername = SCANNER.nextLine();
        System.out.println("Please enter a password.");
        String registerPassword;
        registerPassword = SCANNER.nextLine();
        System.out.println("Please enter the email you'd like to use with this account");
        String registerEmail = SCANNER.nextLine();
        UserData registerRequest = new UserData(registerUsername, registerPassword, registerEmail);
        try {
            String authToken = SERVER.sendRegisterRequest(registerRequest);
            return new PreLoginLoopData(false, authToken);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
            return new PreLoginLoopData(false, null);
        }
    }

    private PreLoginLoopData login() {
        System.out.println("Please enter your username.");
        String loginUsername = SCANNER.nextLine();
        System.out.println("Please enter your password.");
        String loginPassword = SCANNER.nextLine();
        LoginRequest loginRequest = new LoginRequest(loginUsername, loginPassword);
        try {
            String authToken = SERVER.sendLoginRequest(loginRequest);
            if (!mapOfIDsPopulated) {
                boolean populated = populateMapOfIDs(authToken);
                if (populated) {
                    return new PreLoginLoopData(false, authToken);
                }
                else {
                    System.out.println("Sorry, something went wrong with the server. Please try again later.");
                    return new PreLoginLoopData(false, null);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
        }
        return new PreLoginLoopData(false, null);
    }

    private LoginLoopData spectateGame(String authToken) {
        if (mapOfIDs.isEmpty()) {
            System.out.println("There are no current games available to join. Please create a new one.");
            return new LoginLoopData(authToken, 0);
        }
        System.out.println("Please enter the game ID for the game you'd like to spectate (list games to find the game ID)");
        int gameID;
        try {
            gameID = Integer.parseInt(SCANNER.nextLine());
            try {
                UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, mapOfIDs.get(gameID), null);
                SERVER.sendWebsocketRequest(command);
                return new LoginLoopData(authToken, gameID);
            }
            catch (Exception e) {
                System.out.println("Sorry, something went wrong with the server. Please try again later.");
            }
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the game you want to join.");
            System.out.println("If you need the list of games with their IDs, select \"List game\" from the menu.");
        }
        return new LoginLoopData(authToken, 0);
    }

    private void listGames(String authToken) {
        try {
            ArrayList<ListGamesResponse> listOfGameData = SERVER.sendListGamesRequest(authToken);
            if (listOfGameData.isEmpty()) {
                System.out.println("There are no current games. Create a game first.");
                return;
            }
            for (int i = 1; i < listOfGameData.size() + 1; i++) {
                ListGamesResponse currentGame = listOfGameData.get(i - 1);
                String variableWhitePlayer;
                String variableBlackPlayer;
                if (currentGame.whiteUsername() == null) {
                    variableWhitePlayer = "Nobody is currently playing white.";
                }
                else {
                    variableWhitePlayer = "White player is " + currentGame.whiteUsername() + ".";
                }
                if (currentGame.blackUsername() == null) {
                    variableBlackPlayer = "Nobody is currently playing black.";
                }
                else {
                    variableBlackPlayer = "Black player is " + currentGame.blackUsername() + ".";
                }
                System.out.println(i + ": " + currentGame.gameName() + ". " + variableWhitePlayer + " " + variableBlackPlayer);
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
        }
    }

    private void createGame(String authToken) {
        System.out.println("What would you like to name your game session?");
        String gameName = SCANNER.nextLine();
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        try {
            int returnGameID = SERVER.sendCreateGameRequest(createGameRequest, authToken);
            if (returnGameID != -1) {
                mapOfIDs.put(maxIDNumber, returnGameID);
                maxIDNumber++;
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
        }
    }

    private LoginLoopData logout(String authToken) {
        try {
            SERVER.sendLogoutRequest(authToken);
            return new LoginLoopData(null, 0);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
            return new LoginLoopData(authToken, 0);
        }
    }

    private LoginLoopData joinGame(String authToken) {
        if (mapOfIDs.isEmpty()) {
            System.out.println("There are no current games available to join. Please create a new one.");
            return new LoginLoopData(authToken, 0);
        }
        int gameID;
        System.out.println("Which game would you like to join?");
        try {
            gameID = Integer.parseInt(SCANNER.nextLine());
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the game you want to join.");
            System.out.println("If you need the list of games with their IDs, select \"List game\" from the menu.");
            return new LoginLoopData(authToken, 0);
        }
        String color;
        System.out.println("Which color would you like to play as?");
        System.out.println("1. WHITE");
        System.out.println("2. BLACK");
        int colorChoice;
        try {
            colorChoice = Integer.parseInt(SCANNER.nextLine());
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the color you want to play.");
            return new LoginLoopData(authToken, 0);
        }
        if (colorChoice == 1) {
            color = "WHITE";
        }
        else if (colorChoice == 2) {
            color = "BLACK";
        }
        else {
            System.out.println("That is not a valid option. Please type \"1\" for white or \"2\" for black.");
            return new LoginLoopData(authToken, 0);
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, mapOfIDs.get(gameID));
        boolean successfulJoin;
        try {
            successfulJoin = SERVER.sendJoinGameRequest(joinGameRequest, authToken);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
            return new LoginLoopData(authToken, 0);
        }
        if (successfulJoin) {
            try {
                UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, mapOfIDs.get(gameID), null);
                SERVER.sendWebsocketRequest(command);
                return new LoginLoopData(authToken, gameID);
            }
            catch (Exception e) {
                System.out.println("Sorry, something went wrong with the websocket connection.");
            }
        }
        return new LoginLoopData(authToken, 0);
    }

    private void deleteGame(String authToken) {
        System.out.println("Please enter the password to access this functionality.");
        String password = SCANNER.nextLine();
        if (Objects.equals(password, "PaulPhoenixSmasher")) {
            System.out.println("Please enter the game ID of the game you'd like to delete.");
            int gameID = Integer.parseInt(SCANNER.nextLine());
            DeleteGameRequest request = new DeleteGameRequest(mapOfIDs.get(gameID));
            try {
                SERVER.sendDeleteGameRequest(authToken, request);
                updateMapOfIDs(authToken);
            }
            catch (Exception e) {
                System.out.println("Something went wrong with the server. Please try again later.");
            }
        }
    }

    private boolean populateMapOfIDs(String authToken) {
        try {
            ArrayList<ListGamesResponse> listOfGameData = SERVER.sendListGamesRequest(authToken);
            for (ListGamesResponse game : listOfGameData) {
                mapOfIDs.put(maxIDNumber, game.gameID());
                maxIDNumber++;
            }
        }
        catch (Exception e) {
            return false;
        }
        mapOfIDsPopulated = true;
        return true;
    }

    private void updateMapOfIDs(String authToken) {
        try {
            ArrayList<ListGamesResponse> listOfGameData = SERVER.sendListGamesRequest(authToken);
            maxIDNumber = 1;
            mapOfIDs.clear();
            for (ListGamesResponse game: listOfGameData) {
                mapOfIDs.put(maxIDNumber, game.gameID());
                maxIDNumber++;
            }
        }
        catch (Exception e) {
            System.out.println("Failed to update mapOfIDs.");
        }
    }
}
