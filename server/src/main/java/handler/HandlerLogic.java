package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.model;
import service.*;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

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
        } else if (req.pathInfo().equals("/session") && "POST".equals(req.requestMethod())) {
            newRes = LoginHandler(req, res);
        } else if (req.pathInfo().equals("/session") && "DELETE".equals(req.requestMethod())) {
            newRes = LogoutHandler(req, res);
        } else if (req.pathInfo().equals("/game") && "GET".equals(req.requestMethod())) {
            newRes = ListGamesHandler(req, res);
        } else if (req.pathInfo().equals("/game") && "POST".equals(req.requestMethod())){
            newRes = CreateGameHandler(req, res);
        } else if (req.pathInfo().equals("/game") && "PUT".equals(req.requestMethod())) {
            newRes = JoinGameHandler(req, res);
        } else {
            newRes = false;
        }

        return newRes;
    }

    private Object RegisterHandler(Request req, Response res) {
        // deserialize JSON request body to Java request object
        var serializer = new Gson();
        try {
            model.RegisterRequest myRequest = serializer.fromJson(req.body(), model.RegisterRequest.class);
            BadRequestRegister(myRequest);
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

    private Object LoginHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            model.LoginRequest myLoginRequest = serializer.fromJson(req.body(), model.LoginRequest.class);
            BadRequestLogin(myLoginRequest);
            UserService service = new UserService(userDAO, authDAO, gameDAO);
            model.LoginResult myLoginResult = service.login(myLoginRequest);
            return serializer.toJson(myLoginResult);
        } catch (BadRequest e) {
            res.status(400);
            return serializer.toJson(Map.of("message", "Error: bad request"));
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

    private Object LogoutHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            model.LogoutRequest myLogoutRequest = new model.LogoutRequest(req.headers("authorization"));
            UserService service = new UserService(userDAO, authDAO, gameDAO);
            model.LogoutResult myLogoutResult = service.logout(myLogoutRequest);
            return serializer.toJson(myLogoutResult);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

    private Object ListGamesHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            model.ListGameRequest myListGameRequest = new model.ListGameRequest(req.headers("authorization"));
            GameService service = new GameService(userDAO, authDAO, gameDAO);
            model.ListGameResult listGameResult = service.listGames(myListGameRequest);
            return serializer.toJson(listGameResult);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

    private Object CreateGameHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            model.CreateGameRequest myCreateGameRequest = serializer.fromJson(req.body(), model.CreateGameRequest.class);
            BadRequestCreateGame(myCreateGameRequest);
            String authorization = req.headers("authorization");
            GameService service = new GameService(userDAO, authDAO, gameDAO);
            model.CreateGameResult createResult = service.createGame(myCreateGameRequest, authorization);
            return serializer.toJson(createResult);
        } catch (BadRequest e) {
            res.status(400);
            return serializer.toJson(Map.of("message", "Error: bad request"));
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

    private Object JoinGameHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            model.JoinGameRequest myJoinRequest = serializer.fromJson(req.body(), model.JoinGameRequest.class);
            BadRequestJoinGame(myJoinRequest);
            String authorization = req.headers("authorization");
            GameService service = new GameService(userDAO, authDAO, gameDAO);
            model.JoinGameResult joinResult = service.joinGame(myJoinRequest, authorization);
            res.status(200);
            return serializer.toJson(joinResult);
        } catch (BadRequest e) {
            res.status(400);
            return serializer.toJson(Map.of("message", "Error: bad request"));
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (AlreadyTaken e) {
            res.status(403);
            return serializer.toJson(Map.of("message", "Error: already taken"));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: already taken"));
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
    private void BadRequestRegister(model.RegisterRequest registerRequest) throws BadRequest {
        if (registerRequest == null ||
        registerRequest.username() == null || registerRequest.username().isBlank() ||
        registerRequest.password() == null || registerRequest.password().isBlank() ||
        registerRequest.email() == null || registerRequest.email().isBlank()) {
            throw new BadRequest("Error: bad request");
        }
    }

    private void BadRequestLogin(model.LoginRequest loginRequest) throws BadRequest {
        if (loginRequest == null ||
                loginRequest.username() == null || loginRequest.username().isBlank() ||
                loginRequest.password() == null || loginRequest.password().isBlank()) {
            throw new BadRequest("Error: bad request");
        }
    }

    private void BadRequestCreateGame(model.CreateGameRequest createGameRequest) throws BadRequest {
        if (createGameRequest.gameName() == null || createGameRequest.gameName().isBlank()) throw new BadRequest("Error: bad request");
    }

    private void BadRequestJoinGame(model.JoinGameRequest request) throws BadRequest {
        if (request.playerColor() == null || (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK"))) throw new BadRequest("Error: bad request");
        if (request.gameID() == 0) throw new BadRequest("Error: bad request");
    }

}
