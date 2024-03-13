package ui;
import client.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class PreloginUi {
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final ServerFacade server = new ServerFacade("http://localhost:8080");

    public PreloginUi(){

    }

}
