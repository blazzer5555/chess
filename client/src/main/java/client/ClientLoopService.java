package client;

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
    final ChessBoardDrawer DRAWER = new ChessBoardDrawer();
    WebsocketClient wsClient;

    public ClientLoopService() {
        try {
            wsClient = new WebsocketClient();
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong trying to create a websocket");
        }
    }

    public void runLoop() {
        boolean serverStartedCorrectly = populateMapOfIDs();
        if (!serverStartedCorrectly) {
            return;
        }
        System.out.println("Did someone ask to play chess?");
        boolean doneWithProgram = false;
        boolean playingGame = false;
        String authToken = null;
        while (!doneWithProgram) {
            if (authToken != null) {
                if (!playingGame) {
                    LoginLoopData loginLoopData = loginLoop(authToken);
                    authToken = loginLoopData.authToken();
                    playingGame = loginLoopData.playingGame();
                }
                else {
                    playingGame = gameplayLoop(authToken);
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
        String authToken = null;
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
        return new PreLoginLoopData(doneWithProgram, authToken);
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
            return new LoginLoopData(authToken, false);
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
                spectateGame(authToken);
                break;
            case(41):
                deleteGame(authToken);
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return new LoginLoopData(authToken, false);
    }

    private boolean gameplayLoop(String authToken) {
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Redraw chess board\n3. Leave game\n4. Make move\n5. Resign\n6. Highlight legal moves");
        int userResponse;
        try {
            userResponse = Integer.parseInt(SCANNER.nextLine());
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
            return true;
        }
        switch (userResponse) {
            case (1):
                System.out.println("Redraw chess board: Redraws the chess board so you don't have to keep scrolling back up to see it.");
                System.out.println("Leave game: This will make you leave the game, but players can join the open spot, including you.");
                System.out.print("Make move: If it's your turn, make a move on the board.");
                System.out.println("Resign: Forfeit the game. This results in you losing and nobody else being able to makes further moves.");
                System.out.println("Highlight legal moves: Given a place on the board, highlight all the legal moves that piece can make.");
                break;
            case(2):
                redrawBoard();
                break;
            case(3):
                return leaveGame(authToken);
            case(4):
                makeMove(authToken);
                break;
            case(5):
                return resign(authToken);
            case(6):
                highlightLegalMoves(authToken);
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return true;
    }

    private void highlightLegalMoves(String authToken) {
    }

    private boolean resign(String authToken) {
        return false;
    }

    private void makeMove(String authToken) {
    }

    private boolean leaveGame(String authToken) {
        return false;
    }

    private void redrawBoard() {
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
            return new PreLoginLoopData(false, authToken);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
            return new PreLoginLoopData(false, null);
        }
    }

    private void spectateGame(String authToken) {
        System.out.println("Please enter the game ID for the game you'd like to spectate (list games to find the game ID)");
        int spectateGameID;
        try {
            spectateGameID = Integer.parseInt(SCANNER.nextLine());
            int validID = mapOfIDs.get(spectateGameID);
            DRAWER.drawWhitePerspective(null);
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the game you want to join.");
            System.out.println("If you need the list of games with their IDs, select \"List game\" from the menu.");
        }
    }

    private void listGames(String authToken) {
        try {
            ArrayList<ListGamesResponse> listOfGameData = SERVER.sendListGamesRequest(authToken);
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
                    variableBlackPlayer = "Nobody is currently playing white.";
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
            return new LoginLoopData(null, false);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
            return new LoginLoopData(authToken, false);
        }
    }

    private LoginLoopData joinGame(String authToken) {
        if (mapOfIDs.isEmpty()) {
            System.out.println("There are no current games available to join. Please create a new one.");
            return new LoginLoopData(authToken, false);
        }
        int gameID;
        System.out.println("Which game would you like to join?");
        try {
            gameID = Integer.parseInt(SCANNER.nextLine());
            int validID = mapOfIDs.get(gameID);
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the game you want to join.");
            System.out.println("If you need the list of games with their IDs, select \"List game\" from the menu.");
            return new LoginLoopData(authToken, false);
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
            return new LoginLoopData(authToken, false);
        }
        if (colorChoice == 1) {
            color = "WHITE";
        }
        else if (colorChoice == 2) {
            color = "BLACK";
        }
        else {
            System.out.println("That is not a valid option. Please type \"1\" for white or \"2\" for black.");
            return new LoginLoopData(authToken, false);
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, mapOfIDs.get(gameID));
        try {
            boolean successfulJoin = SERVER.sendJoinGameRequest(joinGameRequest, authToken);
            if (successfulJoin) {
                UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, mapOfIDs.get(gameID));
                wsClient.send(command);
                return new LoginLoopData(authToken, true);
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
            return new LoginLoopData(authToken, false);
        }
        return new LoginLoopData(authToken, false);
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

    private boolean populateMapOfIDs() {
        ServerFacade server = new ServerFacade();
        LoginRequest adminLoginRequest = new LoginRequest("david", "kimball");
        String adminAuthToken;
        try {
            adminAuthToken = server.sendLoginRequest(adminLoginRequest);
        }
        catch (Exception e) {
            System.out.println("Failed to startup program. Terminating process.");
            return false;
        }
        try {
            ArrayList<ListGamesResponse> listOfGameData = server.sendListGamesRequest(adminAuthToken);
            for (ListGamesResponse game: listOfGameData) {
                mapOfIDs.put(maxIDNumber, game.gameID());
                maxIDNumber++;
            }
        }
        catch (Exception e) {
            System.out.println("Failed to startup program. Terminating process.");
            return false;
        }
        try {
            server.sendLogoutRequest(adminAuthToken);
        }
        catch (Exception e) {
            System.out.println("Failed to startup program. Terminating process.");
            return false;
        }
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
