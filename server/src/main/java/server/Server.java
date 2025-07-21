package server;

import dataaccess.*;
import spark.*;
import handler.HandlerLogic;


public class Server {
    private UserSQLDAO userDAO;
    private AuthSQLDAO authDAO;
    private GameSQLDAO gameDAO;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // initialize db
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        userDAO = new UserSQLDAO();
        authDAO = new AuthSQLDAO();
        gameDAO = new GameSQLDAO();

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
        Spark.post("/user", new HandlerLogic(userDAO, authDAO, gameDAO));
        Spark.post("/session", new HandlerLogic(userDAO, authDAO, gameDAO));
        Spark.delete("/session", new HandlerLogic(userDAO, authDAO, gameDAO));
        Spark.get("/game", new HandlerLogic(userDAO, authDAO, gameDAO));
        Spark.post("/game", new HandlerLogic(userDAO, authDAO, gameDAO));
        Spark.put("/game", new HandlerLogic(userDAO, authDAO, gameDAO));
        Spark.delete("/db", new HandlerLogic(userDAO, authDAO, gameDAO));
    }

}
