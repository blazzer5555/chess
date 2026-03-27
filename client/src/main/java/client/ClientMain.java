package client;

import model.*;
import java.util.*;

public class ClientMain {

    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServerFacade server = new ServerFacade();
        System.out.println("Did someone ask to play chess?");
        boolean doneWithProgram = false;
        String authToken = null;
        while (!doneWithProgram) {
            if (authToken != null) {
                authToken = processLoginLoop(scanner, server, authToken);
            }
            else {
                PreLoginLoopData userStatuses = processPreLoginLoop(scanner, server);
                doneWithProgram = userStatuses.doneWithProgram();
                authToken = userStatuses.authToken();
            }
        }
        System.out.println("Thanks for playing!");
    }

    private static PreLoginLoopData processPreLoginLoop(Scanner scanner, ServerFacade server) {
        boolean doneWithProgram = false;
        String authToken = null;
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Quit\n3. Log in\n4. Register");
        int userResponse = scanner.nextInt();
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
                String loginUsername;
                loginUsername = scanner.nextLine();
                System.out.println("Please enter your password.");
                String loginPassword = scanner.nextLine();
                LoginRequest loginRequest = new LoginRequest(loginUsername, loginPassword);
                authToken = server.sendLoginRequest(loginRequest);
                break;
            case (4):
                System.out.println("Please enter a username.");
                String registerUsername;
                registerUsername = scanner.nextLine();
                System.out.println("Please enter a password.");
                String registerPassword;
                registerPassword = scanner.nextLine();
                System.out.println("Please enter the email you'd like to use with this account");
                String registerEmail = scanner.nextLine();
                UserData registerRequest = new UserData(registerUsername, registerPassword, registerEmail);
                server.sendRegisterRequest(registerRequest);
                break;
        }
        return new PreLoginLoopData(doneWithProgram, authToken);
    }

    private static String processLoginLoop(Scanner scanner, ServerFacade server, String authToken) {
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Log out\n3. Create game\n4. List games\n5. Play game\n6. Spectate game");
        int userResponse = scanner.nextInt();
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
                System.out.println("Please enter the game ID for the game you wnt to join (List games to find the game ID)");
                int joinGameID = scanner.nextInt();
                System.out.println("Please enter the color that you'd like to play.");
                String color = scanner.nextLine();
                JoinGameRequest joinGameRequest = new JoinGameRequest(color, joinGameID);
                server.sendJoinGameRequest(joinGameRequest);
                // DRAW CHESS BOARD
                break;
            case(6):
                System.out.println("Please enter the game ID for the game you'd like to spectate (list games to find the game ID)");
                int spectateGameID = scanner.nextInt();
                // DRAW CHESS BOARD FOR THE GIVEN ID
                break;
        }
        return authToken;
    }
}
