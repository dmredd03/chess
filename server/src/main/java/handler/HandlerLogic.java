package handler;

import dataaccess.*;
import model.Model;
import service.*;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.Map;

public class HandlerLogic implements Route {
    private UserSQLDAO userDAO;
    private AuthSQLDAO authDAO;
    private GameSQLDAO gameDAO;

    public HandlerLogic(UserSQLDAO userDAO, AuthSQLDAO authDAO, GameSQLDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }


    public Object handle(Request req, Response res) {
        Object newRes;
        if (req.pathInfo().equals("/user")) {
            newRes = registerHandler(req, res);
        } else if (req.pathInfo().equals("/db")) {
            newRes = clearHandler(req, res);
        } else if (req.pathInfo().equals("/session") && "POST".equals(req.requestMethod())) {
            newRes = loginHandler(req, res);
        } else if (req.pathInfo().equals("/session") && "DELETE".equals(req.requestMethod())) {
            newRes = logoutHandler(req, res);
        } else if (req.pathInfo().equals("/game") && "GET".equals(req.requestMethod())) {
            newRes = listGamesHandler(req, res);
        } else if (req.pathInfo().equals("/game") && "POST".equals(req.requestMethod())){
            newRes = createGameHandler(req, res);
        } else if (req.pathInfo().equals("/game") && "PUT".equals(req.requestMethod())) {
            newRes = joinGameHandler(req, res);
        } else {
            newRes = false;
        }

        return newRes;
    }

    private Object registerHandler(Request req, Response res) {
        // deserialize JSON request body to Java request object
        var serializer = new Gson();
        try {
            Model.RegisterRequest myRequest = serializer.fromJson(req.body(), Model.RegisterRequest.class);
            badRequestRegister(myRequest);
            // Call service.service class to perform the requested function, passing it the request
            UserService service = new UserService(userDAO, authDAO, gameDAO);
            // Receive java response object from service.service
            Model.RegisterResult registerResult = service.register(myRequest);
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

    private Object loginHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            Model.LoginRequest myLoginRequest = serializer.fromJson(req.body(), Model.LoginRequest.class);
            badRequestLogin(myLoginRequest);
            UserService service = new UserService(userDAO, authDAO, gameDAO);
            Model.LoginResult myLoginResult = service.login(myLoginRequest);
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

    private Object logoutHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            Model.LogoutRequest myLogoutRequest = new Model.LogoutRequest(req.headers("authorization"));
            UserService service = new UserService(userDAO, authDAO, gameDAO);
            Model.LogoutResult myLogoutResult = service.logout(myLogoutRequest);
            return serializer.toJson(myLogoutResult);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

    private Object listGamesHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            Model.ListGameRequest myListGameRequest = new Model.ListGameRequest(req.headers("authorization"));
            GameService service = new GameService(userDAO, authDAO, gameDAO);
            Model.ListGameResult listGameResult = service.listGames(myListGameRequest);
            return serializer.toJson(listGameResult);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(Map.of("message", "Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }

    private Object createGameHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            Model.CreateGameRequest myCreateGameRequest = serializer.fromJson(req.body(), Model.CreateGameRequest.class);
            badRequestCreateGame(myCreateGameRequest);
            String authorization = req.headers("authorization");
            GameService service = new GameService(userDAO, authDAO, gameDAO);
            Model.CreateGameResult createResult = service.createGame(myCreateGameRequest, authorization);
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

    private Object joinGameHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            Model.JoinGameRequest myJoinRequest = serializer.fromJson(req.body(), Model.JoinGameRequest.class);
            badRequestJoinGame(myJoinRequest);
            String authorization = req.headers("authorization");
            GameService service = new GameService(userDAO, authDAO, gameDAO);
            Model.JoinGameResult joinResult = service.joinGame(myJoinRequest, authorization);
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

    private Object clearHandler(Request req, Response res) {
        var serializer = new Gson();
        try {
            Model.ClearRequest myRequest = serializer.fromJson(req.body(), Model.ClearRequest.class);
            ClearService service = new ClearService(userDAO, authDAO, gameDAO);
            Model.ClearResult clearResult = service.clear();
            return serializer.toJson(clearResult);
        } catch (SQLException | DataAccessException e) {
            res.status(500);
            return serializer.toJson(Map.of("message", "Error: (" + e.getMessage() + ")"));
        }
    }
// consider moving badrequest checks somewhere else?
    private void badRequestRegister(Model.RegisterRequest registerRequest) throws BadRequest {
        if (registerRequest == null ||
        registerRequest.username() == null || registerRequest.username().isBlank() ||
        registerRequest.password() == null || registerRequest.password().isBlank() ||
        registerRequest.email() == null || registerRequest.email().isBlank()) {
            throw new BadRequest("Error: bad request");
        }
    }

    private void badRequestLogin(Model.LoginRequest loginRequest) throws BadRequest {
        if (loginRequest == null ||
                loginRequest.username() == null || loginRequest.username().isBlank() ||
                loginRequest.password() == null || loginRequest.password().isBlank()) {
            throw new BadRequest("Error: bad request");
        }
    }

    private void badRequestCreateGame(Model.CreateGameRequest createGameRequest) throws BadRequest {
        if (createGameRequest.gameName() == null || createGameRequest.gameName().isBlank()) { throw new BadRequest("Error: bad request"); }
    }

    private void badRequestJoinGame(Model.JoinGameRequest request) throws BadRequest {
        if (request.playerColor() == null ||
                (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK"))) { throw new BadRequest("Error: bad request"); }
        if (request.gameID() == 0) { throw new BadRequest("Error: bad request"); }
    }

}
