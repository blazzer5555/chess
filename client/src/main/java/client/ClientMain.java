package client;

import chess.*;

import java.util.Objects;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Did someone ask to play chess?");
        boolean doneWithProgram = false;
        while (!doneWithProgram) {
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
                    String loginUsername = scanner.nextLine();
                    loginUsername = scanner.nextLine();
                    System.out.println("Please enter your password.");
                    String loginPassword = scanner.nextLine();
                    break;
                case (4):
                    System.out.println("Please enter a username.");
                    String registerUsername = scanner.nextLine();
                    registerUsername = scanner.nextLine();
                    boolean userConfirmedPassword = false;
                    while (!userConfirmedPassword) {
                        userConfirmedPassword = true;
                        System.out.println("Please enter a password.");
                        String registerPassword = scanner.nextLine();
                        System.out.println("Please confirm your password.");
                        String confirmPassword = scanner.nextLine();
                        if (!Objects.equals(registerPassword, confirmPassword)) {
                            System.out.println("Passwords do not match, please retry.");
                            userConfirmedPassword = false;
                        }
                    }
                    System.out.println("Please enter the email you'd like to use with this account");
                    String registerEmail = scanner.nextLine();
                    break;
            }
        }
        System.out.println("Thanks for playing!");
    }
}
