package ui;

import chess.ChessGame;
import client.ServerFacade;
import exception.ResponseException;
import model.GameData;
import response.CreateGameResponse;
import response.ListGameResponse;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

public class PostLoginUi {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final ServerFacade server = new ServerFacade("http://localhost:3000");
    private final String authToken;

    public PostLoginUi(String authToken){
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
            else if(Objects.equals(line, "Join as Observer")|| Objects.equals(line, "join as observer")|| Objects.equals(line, "5")){
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
            out.println();
            logout(scanner);
        }
    }
    public void createGame(Scanner scanner){
        try{
            out.print("What do you want to call your game? ");
            String name = scanner.nextLine();
            server.createGame(name, authToken);
            out.println("Your game has been created!");
            out.println();
            loggedIn(scanner);
        }
        catch(ResponseException e){
            out.println("Error creating game. Please try again.");
            createGame(scanner);
        }
    }
    public void joinGame(Scanner scanner){
        try{
//            list valid games
            list();

            out.println("What game do you want to join?");
            Integer gameNumber = scanner.nextInt();
            scanner.nextLine();

            out.println("Which team do you want to be? (W)White or (B)Black or (O)Observer?");
            String color = scanner.nextLine();
            ChessGame.TeamColor teamColor;
            if(color.equals("Black")|| color.equals("B") ||  color.equals("b") ){
                teamColor = ChessGame.TeamColor.BLACK;
            }else if(color.equals("White")||color.equals("W")||  color.equals("w") ){
                teamColor = ChessGame.TeamColor.WHITE;
            }else{
                teamColor = null;
            }

            out.println();
            ListGameResponse response = server.listGames(authToken);
            GameData game = response.games().get(gameNumber-1);
            server.joinGame(game.getGameID(), teamColor, authToken);

            new GamePlayUi(game.getGame(), teamColor, authToken);

        }
        catch(ResponseException e){
            out.println("Error joining game. Please try again.");
            joinGame(scanner);
        }
    }

    public void list() throws ResponseException {
        ListGameResponse response = server.listGames(authToken);
        if (response != null && response.games() != null && !response.games().isEmpty()) {
            int gameNumber = 1;
            for (GameData gameData : response.games()) {
                // Display game information (name and players) in a numbered list format
                System.out.println(gameNumber + ")" +" Game Name: " + gameData.getGameName() + "  White player:("+ gameData.getWhiteUsername() + ")  Black player:(" + gameData.getBlackUsername()+ ")");
                gameNumber++;
            }
        } else {
            System.out.println("No games available.");
        }
    }
    public void listGames(Scanner scanner){
        try {
            list();
            out.println();
            loggedIn(scanner);
        }catch(ResponseException e){
            out.println("Error listing games. Please try again.");
            listGames(scanner);
        }
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
