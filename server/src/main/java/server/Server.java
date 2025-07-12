package server;

import dataaccess.MemoryGameDAO;
import spark.*;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import handler.HandlerLogic;
//TODO: import GameDAO when implemented

public class Server {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // initialize db
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        // Register your endpoints and handle exceptions here.
        createRoutes();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        // Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createRoutes() {
        Spark.post("/user", new handler.HandlerLogic(userDAO, authDAO, gameDAO));
    }

}
