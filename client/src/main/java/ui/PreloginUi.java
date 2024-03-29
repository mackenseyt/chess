package ui;
import client.ServerFacade;
import exception.ResponseException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import static java.lang.System.exit;

import static ui.EscapeSequences.*;

public class PreloginUi {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final ServerFacade server = new ServerFacade("http://localhost:3000");

    public PreloginUi() throws ResponseException {
        Scanner scanner = new Scanner(System.in);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("LETS PLAY CHESS!");
        start(scanner);
    }

    public void start(Scanner scanner){
        options();

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(Objects.equals(line, "Register")|| Objects.equals(line, "register")|| Objects.equals(line, "1")){
                registerUI(scanner);
            }
            else if(Objects.equals(line, "Login")|| Objects.equals(line, "login")|| Objects.equals(line, "2")){
                loginUI(scanner);
            }
            else if(Objects.equals(line, "Quit")|| Objects.equals(line, "quit")|| Objects.equals(line, "3")){
                exit(0);
            }
            else if(Objects.equals(line, "Help")|| Objects.equals(line, "help")|| Objects.equals(line, "4")){
                options();
            }
        }
    }

    private void options(){
        out.println("What do you want to do?");
        out.println("1: Register");
        out.println("2: Login");
        out.println("3: Quit");
        out.println("4: Help");
    }
    private void registerUI(Scanner scanner){
        try {
            out.print("Desired Username: ");
            String username = scanner.nextLine();
            out.print("Create Password: ");
            String password = scanner.nextLine();
            out.print("Email: ");
            String email = scanner.nextLine();

            var response = server.register(username, password, email);
            if (response != null) {
                out.println("Successfully Registered");
                out.println();
                String authToken = response.authToken();
                new PostLoginUi(authToken);
            }
        }catch(ResponseException e){
            out.println("Failed to register. Please try again.");
            registerUI(scanner);
        }
    }
    private void loginUI(Scanner scanner){
        try{
            out.print("Username:");
            String username = scanner.nextLine();
            out.print("Password: ");
            String password = scanner.nextLine();

            var response = server.login(username, password);
            if(response != null){
                out.println("Login Successful");
                out.println();
                String authToken = response.authToken();
                new PostLoginUi(authToken);
            }
        }
        catch(ResponseException e){
            out.println("Failed to login. Try again.");
            loginUI(scanner);
        }
    }
}
