package client;

import model.*;
import java.util.Scanner;

public class ClientMain {

    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServerFacade server = new ServerFacade();
        System.out.println("Did someone ask to play chess?");
        boolean doneWithProgram = false;
        boolean loggedIn = false;
        while (!doneWithProgram) {
            if (loggedIn) {
                loggedIn = processLoginLoop(scanner, server);
            }
            else {
                boolean[] userStatuses = processPreLoginLoop(scanner, server);
                doneWithProgram = userStatuses[0];
                loggedIn = userStatuses[1];
            }
        }
        System.out.println("Thanks for playing!");
    }

    private static boolean[] processPreLoginLoop(Scanner scanner, ServerFacade server) {
        boolean doneWithProgram = false;
        boolean loggedIn = false;
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
                server.sendLoginRequest(loginRequest);
                loggedIn = true;
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
                loggedIn = true;
                break;
        }
        boolean[] returnBooleans = new boolean[2];
        returnBooleans[0] = doneWithProgram;
        returnBooleans[1] = loggedIn;
        return returnBooleans;
    }

    private static boolean processLoginLoop(Scanner scanner, ServerFacade server) {
        boolean loggedIn = true;
        System.out.println("Type the number associated with the action, then press enter. \n");
        System.out.println("1. Help\n2. Log out\n3. Create game\n4. List games\n5.Play game\n6. Spectate game");
        int userResponse = scanner.nextInt();
        switch (userResponse) {
            case (1):
                break;
            case(2):
                break;
            case(3):
                break;
            case(4):
                break;
            case(5):
                break;
            case(6):
                break;
        }
        return loggedIn;
    }
}
