package ui;

import chess.ChessGame;
import client.ServerFacade;
import exception.ResponseException;
import response.CreateGameResponse;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

public class PostLoginUi {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final ServerFacade server = new ServerFacade("http://localhost:3000");
    private final String authToken;

    public PostLoginUi(String authToken) throws ResponseException {
        this.authToken = authToken;
        Scanner scanner = new Scanner(System.in);
        loggedIn(scanner);

    }

    private void loggedIn(Scanner scanner){
        options();
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(Objects.equals(line, "Logout")|| Objects.equals(line, "logout")|| Objects.equals(line, "1")){
                logout(scanner);
            }
            else if(Objects.equals(line, "Create a Game")|| Objects.equals(line, "create a game")|| Objects.equals(line, "2")){
                createGame(scanner);
            }
            else if(Objects.equals(line, "List Games")|| Objects.equals(line, "list games")|| Objects.equals(line, "3")){
                listGames(scanner);
            }
            else if(Objects.equals(line, "Join Game")|| Objects.equals(line, "join game")|| Objects.equals(line, "4")){
                joinGame(scanner);
            }
            else if(Objects.equals(line, "Help")|| Objects.equals(line, "help")|| Objects.equals(line, "6")){
                out.println("Input either the number or the name of the command you want to run.");
                options();
            }
            else if(Objects.equals(line, "Quit")|| Objects.equals(line, "quit")|| Objects.equals(line, "7")){
                exit(0);
            }
        }
    }
    public void logout(Scanner scanner){
        try{
            server.logout(authToken);
            out.println("Logged out");
            new PreloginUi();
        }catch(ResponseException e){
            out.println("Error logging out");
            loggedIn(scanner);
        }
    }
    public void createGame(Scanner scanner){
        try{
            out.println("What do you want to call your game?");
            String name = scanner.nextLine();
            server.createGame(name, authToken);
            out.println("Your game has been created!");
            loggedIn(scanner);
        }
        catch(ResponseException e){
            out.println("Error creating game. Please try again.");
            loggedIn(scanner);
        }
    }
    public void joinGame(Scanner scanner){
        try{
            out.println("What is the ID of the game you want to join?");
            Integer id = scanner.nextInt();
            out.println("Which team do you want to be? Black/White/just watch");
            String color = scanner.nextLine();
            ChessGame.TeamColor teamColor;
            if(color.equals("Black")){
                teamColor = ChessGame.TeamColor.BLACK;
            }else if(color.equals("White")){
                teamColor = ChessGame.TeamColor.WHITE;
            }else{
                teamColor = null;
            }
            server.joinGame(id, teamColor, authToken);
            new GamePlayUi();

        }
        catch(ResponseException e){
            out.println("Error joining game. Please try again.");
            loggedIn(scanner);
        }
    }
    public void listGames(Scanner scanner){

    }

    private void options() {
        out.println("You're Logged in. What do you want to do?");
        out.println("1: Logout");
        out.println("2: Create a Game");
        out.println("3: List Games");
        out.println("4: Join Game");
        out.println("5: Join as Observer");
        out.println("6: Help");
        out.println("7: Quit");
    }
}
