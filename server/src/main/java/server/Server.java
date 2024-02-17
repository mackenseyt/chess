package server;

import model.AuthData;
import spark.*;
import Service.*;
import java.nio.file.Paths;
import com.google.gson.Gson;

public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearDatabase);
        Spark.post("/user", this::registerUser);
        Spark.exception(Exception.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(Exception ex, Request request, Response response) {
        response.status(ex.getStatusCode());
    }

    private Object registerUser(Request request, Response response) throws Exception {
        throw new RuntimeException();
    }

    private Object clearDatabase(Request request, Response response) {

        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}