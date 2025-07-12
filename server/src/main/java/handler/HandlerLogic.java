package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.model;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import service.UserService;

import java.util.Map;

public class HandlerLogic implements Route {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public HandlerLogic(MemoryUserDAO userDAO, MemoryAuthDAO authDAO, MemoryGameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }


    public Object handle(Request req, Response res) {
        Object newRes;
        if (req.pathInfo().equals("/user")) {
            newRes = RegisterHandler(req, res);
        } else {
            newRes = null;
        }

        return newRes;
    }

    private Object RegisterHandler(Request req, Response res) {
        // deserialize JSON request body to Java request object
        var serializer = new Gson();
        try {
            model.RegisterRequest myRequest = serializer.fromJson(req.body(), model.RegisterRequest.class);

            // Call service.service class to perform the requested function, passing it the request
            UserService service = new UserService(userDAO, authDAO, gameDAO);
            // Receive java response object from service.service
            model.RegisterResult registerResult = service.register(myRequest);
            // Serialize Java response object to JSON
            //Send HTTP response back to client with status code and response body
            return serializer.toJson(registerResult);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            res.body("error");
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

}
