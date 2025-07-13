package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.model;
import service.BadRequest;
import service.ClearService;
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
        } else if (req.pathInfo().equals("/db")) {
            newRes = ClearHandler(req, res);
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
            BadRequestRegister(serializer, myRequest);
            // Call service.service class to perform the requested function, passing it the request
            UserService service = new UserService(userDAO, authDAO, gameDAO);
            // Receive java response object from service.service
            model.RegisterResult registerResult = service.register(myRequest);
            // Serialize Java response object to JSON
            //Send HTTP response back to client with status code and response body
            return serializer.toJson(registerResult);
        } catch (DataAccessException e) {
            res.status(403);
            return serializer.toJson(Map.of("message", "Error: already taken"));
        } catch (BadRequest e) {
            res.status(400);
            return serializer.toJson(Map.of("message", "Error: bad request"));
        } catch (Exception e) {
            res.status(500);
            res.body("error");
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

    private Object ClearHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            model.ClearRequest myRequest = serializer.fromJson(req.body(), model.ClearRequest.class);
            ClearService service = new ClearService(userDAO, authDAO, gameDAO);
            model.ClearResult clearResult = service.clear();
            return serializer.toJson(clearResult);
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }
// consider moving badrequest checks somewhere else?
    private void BadRequestRegister(Gson gson, model.RegisterRequest registerRequest) throws BadRequest {
        if (registerRequest == null ||
        registerRequest.username() == null || registerRequest.username().isBlank() ||
        registerRequest.password() == null || registerRequest.username().isBlank() ||
        registerRequest.email() == null || registerRequest.email().isBlank()) {
            throw new BadRequest("Error: bad request");
        }
    }

}
