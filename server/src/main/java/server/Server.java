package server;

import spark.*;
import java.nio.file.Paths;

public class Server {
/** code given in project description. Not sure why it's different. Might delete later **/
//    public int run(int desiredPort) {
//        Spark.port(desiredPort);
//
//        var webDir = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "web");
//        Spark.externalStaticFileLocation(webDir.toString());
//
//        // Register your endpoints and handle exceptions here.
//
//        Spark.awaitInitialization();
//        return Spark.port();
//    }
//
//    public void stop() {
//        Spark.stop();
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}