package client;

import model.*;
import java.util.*;

public class ClientMain {

    static Map<Integer, Integer> mapOfIDs = new HashMap<>();
    static int maxIDNumber = 1;
    static final Scanner SCANNER = new Scanner(System.in);
    static final ServerFacade SERVER = new ServerFacade();
    static final ChessBoardDrawer DRAWER = new ChessBoardDrawer();

    static void main(String[] args) {
        boolean serverStartedCorrectly = populateMapOfIDs();
        if (!serverStartedCorrectly) {
            return;
        }
        System.out.println("Did someone ask to play chess?");
        boolean doneWithProgram = false;
        String authToken = null;
        while (!doneWithProgram) {
            if (authToken != null) {
                authToken = processLoginLoop(authToken);
            }
            else {
                PreLoginLoopData userStatuses = processPreLoginLoop();
                doneWithProgram = userStatuses.doneWithProgram();
                authToken = userStatuses.authToken();
            }
        }
        System.out.println("Thanks for playing!");
    }

    private static PreLoginLoopData processPreLoginLoop() {
        boolean doneWithProgram = false;
        String authToken = null;
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Quit\n3. Log in\n4. Register");
        int userResponse;
        try {
            userResponse = SCANNER.nextInt();
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
            return new PreLoginLoopData(doneWithProgram, authToken);
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
                System.out.println("Please enter your username.");
                String loginUsername = SCANNER.nextLine();
                loginUsername = SCANNER.nextLine();
                System.out.println("Please enter your password.");
                String loginPassword = SCANNER.nextLine();
                LoginRequest loginRequest = new LoginRequest(loginUsername, loginPassword);
                try {
                    authToken = SERVER.sendLoginRequest(loginRequest);
                }
                catch (Exception e) {
                    System.out.println("Sorry, something went wrong with the server. Please try again later.");
                }
                break;
            case (4):
                System.out.println("Please enter a username.");
                String registerUsername = SCANNER.nextLine();
                registerUsername = SCANNER.nextLine();
                System.out.println("Please enter a password.");
                String registerPassword;
                registerPassword = SCANNER.nextLine();
                System.out.println("Please enter the email you'd like to use with this account");
                String registerEmail = SCANNER.nextLine();
                UserData registerRequest = new UserData(registerUsername, registerPassword, registerEmail);
                try {
                    authToken = SERVER.sendRegisterRequest(registerRequest);

                }
                catch (Exception e) {
                    System.out.println("Sorry, something went wrong with the server. Please try again later.");
                }
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return new PreLoginLoopData(doneWithProgram, authToken);
    }

    private static String processLoginLoop(String authToken) {
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Log out\n3. Create game\n4. List games\n5. Play game\n6. Spectate game");
        int userResponse;
        try {
            userResponse = SCANNER.nextInt();
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
            return authToken;
        }
        switch (userResponse) {
            case (1):
                System.out.println("Log out: log out of the program and go back to the home screen.");
                System.out.println("Create game: Create a new chess game. You will have to join the game separately.");
                System.out.println("List games: List all the games currently available, along with their IDs, and who's playing which color.");
                System.out.println("Play game: Join a game as either the white or black player");
                System.out.println("Spectate game: Observe a game that is being played by other players.");
                break;
            case(2):
                try {
                    SERVER.sendLogoutRequest(authToken);
                    return null;
                }
                catch (Exception e) {
                    System.out.println("Sorry, something went wrong with the server. Please try again later.");
                }
                break;
            case(3):
                System.out.println("What would you like to name your game session?");
                String gameName = SCANNER.nextLine();
                gameName = SCANNER.nextLine();
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
                break;
            case(4):
                try {
                    ArrayList<ListGamesResponse> listOfGameData = SERVER.sendListGamesRequest(authToken);
                    for (int i = 1; i < listOfGameData.size() + 1; i++) {
                        ListGamesResponse currentGame = listOfGameData.get(i - 1);
                        System.out.print(i + ": " + currentGame.gameName() + ". White player is " + currentGame.whiteUsername());
                        System.out.println(", and black player is " + currentGame.blackUsername() + ".");
                    }
                }
                catch (Exception e) {
                    System.out.println("Sorry, something went wrong with the server. Please try again later.");
                }
                break;
            case(5):
                switchCase5InLoggedInLoop(authToken);
                break;
            case(6):
                System.out.println("Please enter the game ID for the game you'd like to spectate (list games to find the game ID)");
                int spectateGameID;
                try {
                    spectateGameID = SCANNER.nextInt();
                    int validID = mapOfIDs.get(spectateGameID);
                }
                catch (Exception e) {
                    System.out.println("That is not a valid input. Please type the number associated with the game you want to join.");
                    System.out.println("If you need the list of games with their IDs, select \"List game\" from the menu.");
                    return authToken;
                }
                DRAWER.drawWhitePerspective();
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return authToken;
    }

    private static void switchCase5InLoggedInLoop(String authToken) {
        if (mapOfIDs.isEmpty()) {
            System.out.println("There are no current games available to join. Please create a new one.");
            return;
        }
        int gameID;
        System.out.println("Which game would you like to join?");
        try {
            gameID = SCANNER.nextInt();
            int validID = mapOfIDs.get(gameID);
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the game you want to join.");
            System.out.println("If you need the list of games with their IDs, select \"List game\" from the menu.");
            return;
        }
        String color = "";
        System.out.println("Which color would you like to play as?");
        System.out.println("1. WHITE");
        System.out.println("2. BLACK");
        int colorChoice;
        try {
            colorChoice = SCANNER.nextInt();
        }
        catch (Exception e) {
            System.out.println("That is not a valid input. Please type the number associated with the color you want to play.");
            return;
        }
        if (colorChoice == 1) {
            color = "WHITE";
        }
        else if (colorChoice == 2) {
            color = "BLACK";
        }
        else {
            System.out.println("That is not a valid option. Please type \"1\" for white or \"2\" for black.");
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, mapOfIDs.get(gameID));
        try {
            SERVER.sendJoinGameRequest(joinGameRequest, authToken);
        }
        catch (Exception e) {
            System.out.println("Sorry, something went wrong with the server. Please try again later.");
        }
        if (color.equals("WHITE")) {
            DRAWER.drawWhitePerspective();
        }
        else {
            DRAWER.drawBlackPerspective();
        }
    }

    private static boolean populateMapOfIDs() {
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
}
