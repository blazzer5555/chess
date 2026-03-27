package client;

import model.*;
import java.util.*;

public class ClientMain {

    static void main(String[] args) {
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
        Scanner scanner = new Scanner(System.in);
        ServerFacade server = new ServerFacade();
        boolean doneWithProgram = false;
        String authToken = null;
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Quit\n3. Log in\n4. Register");
        int userResponse = 0;
        try {
            userResponse = scanner.nextInt();
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
                String loginUsername = scanner.nextLine();
                loginUsername = scanner.nextLine();
                System.out.println("Please enter your password.");
                String loginPassword = scanner.nextLine();
                LoginRequest loginRequest = new LoginRequest(loginUsername, loginPassword);
                authToken = server.sendLoginRequest(loginRequest);
                break;
            case (4):
                System.out.println("Please enter a username.");
                String registerUsername = scanner.nextLine();
                registerUsername = scanner.nextLine();
                System.out.println("Please enter a password.");
                String registerPassword;
                registerPassword = scanner.nextLine();
                System.out.println("Please enter the email you'd like to use with this account");
                String registerEmail = scanner.nextLine();
                UserData registerRequest = new UserData(registerUsername, registerPassword, registerEmail);
                server.sendRegisterRequest(registerRequest);
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return new PreLoginLoopData(doneWithProgram, authToken);
    }

    private static String processLoginLoop(String authToken) {
        Scanner scanner = new Scanner(System.in);
        ServerFacade server = new ServerFacade();
        ChessBoardDrawer drawer = new ChessBoardDrawer();
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Log out\n3. Create game\n4. List games\n5. Play game\n6. Spectate game");
        int userResponse = 0;
        try {
            userResponse = scanner.nextInt();
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
                server.sendLogoutRequest(authToken);
                return null;
            case(3):
                System.out.println("What would you like to name your game session?");
                String gameName = scanner.nextLine();
                CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
                server.sendCreateGameRequest(createGameRequest);
                break;
            case(4):
                ArrayList<ListGamesReturnData> listOfGameData = server.sendListGamesRequest();
                break;
            case(5):
                boolean choseValidGame = false;
                int gameID = 0;
                while (!choseValidGame) {
                    System.out.println("Which game would you like to join?");
                    try {
                        gameID = scanner.nextInt();
                        choseValidGame = true;
                    } catch (Exception e) {
                        System.out.println("That is not a valid option. Please type the number associated with the game you want to join.");
                        System.out.println("If you need the list of games with their IDs, select \"List game\" from the menu.");
                    }
                }
                boolean choseValidColor = false;
                String color = "";
                while (!choseValidColor) {
                    System.out.println("Which color would you like to play as?");
                    System.out.println("1. WHITE");
                    System.out.println("2. BLACK");
                    int colorChoice = 0;
                    try {
                        colorChoice = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("That is not a valid option. Please type the number associated with the color you want to play.");
                        continue;
                    }
                    if (colorChoice == 1) {
                        color = "WHITE";
                        choseValidColor = true;
                    } else if (colorChoice == 2) {
                        color = "BLACK";
                        choseValidColor = true;
                    } else {
                        System.out.println("That is not a valid option. Please type the number associated with the color you want to play.");
                    }
                }
                JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID);
                server.sendJoinGameRequest(joinGameRequest);
                if (color.equals("WHITE")) {
                    drawer.drawWhitePerspective();
                }
                else {
                    drawer.drawBlackPerspective();
                }
                break;
            case(6):
                System.out.println("Please enter the game ID for the game you'd like to spectate (list games to find the game ID)");
                int spectateGameID = 0;
                try {
                    spectateGameID = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("That is not a valid option. Please type the number associated with the game you want to spectate.");
                    return authToken;
                }
                drawer.drawWhitePerspective();
                break;
            default:
                System.out.println("That is not a valid input. Please type the number associated with the option you'd like to choose.");
                break;
        }
        return authToken;
    }
}
