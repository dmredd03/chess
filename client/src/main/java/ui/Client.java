package ui;

import chess.ChessGame;
import model.Model;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private final ServerFacade server;
    private String authorization;
    private Map<Integer, Integer> gameNumberTogameID = new HashMap<>(); // gameNumber, gameID

    public Client(String serverUrl) {
        gameNumberTogameID = new HashMap<>();
        server = new ServerFacade(serverUrl);
    }

    public String preloginEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> helpPrelogin;
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private final String helpPrelogin = "Commands:\n" +
                "Login to existing account: login <username> <password>\n" +
                "Register new account: register <username> <password> <email>\n" +
                "Quit Program: quit\n" +
                "Help for commands: help\n";

    public String login(String... params) throws ResponseException {
        // implement login code here
        if (params.length >= 2) {
            Model.LoginRequest request = new Model.LoginRequest(params[0], params[1]);
            Model.LoginResult result = server.login(request);
            server.setAuthorization(result.authToken());
            authorization = result.authToken();

            return "Successfully logged in as " + params[0] + "\n";
        }
        throw new ResponseException(400, "Usage: login <username> <password>\n");
    }

    public String register(String... params) throws ResponseException {
        // register code
        if (params.length >= 3) {
            Model.RegisterRequest request = new Model.RegisterRequest(params[0], params[1], params[2]);
            Model.RegisterResult result = server.register(request);
            server.setAuthorization(result.authToken());
            authorization = result.authToken();

            return "Successfully registered as " + params[0] + "\n";
        }
        throw new ResponseException(400, "Usage: register <username> <password> <email>\n");
    }

    public String postloginEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(params);
                case "creategame" -> createGame(params);
                case "listgame" -> listGame();
                case "playgame" -> playGame(params);
                case "observegame" -> observeGame(params);
                case "quit" -> "quit";
                default -> helpPostlogin;
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private final String helpPostlogin = "Commands:\n" +
            "Logout of current user: logout\n" +
            "Create a new game: creategame <game name>\n" +
            "List activate games: listgame\n" +
            "Play a game: playgame <game #>\n" +
            "Observe a current game: observegame <game #>\n" +
            "Quit Program: quit\n" +
            "Help for commands: help\n";;

    public String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            Model.LogoutRequest request = new Model.LogoutRequest(authorization);
            server.logout(request);
            authorization = null;
            server.clearAuthorization();
            gameNumberTogameID.clear();

            return "Successfully logged out\n";
        }
        throw new ResponseException(400, "Usage: logout\n");
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            Model.CreateGameRequest request = new Model.CreateGameRequest(params[0]);
            Model.CreateGameResult result = server.createGame(request);

            return "Created game " + params[0] + "\n";
        }
        throw new ResponseException(400, "Usage: creategame <game name>\n");
    }

    public String listGame() throws ResponseException {
        if (gameNumberTogameID.isEmpty()) { return "No active games\n"; }

        Model.ListGameRequest request = new Model.ListGameRequest(server.getAuthorization());
        Model.ListGameResult result = server.listGame(request);

        String list = "";
        int i = 1;
        gameNumberTogameID.clear();
        for (Model.PrintGameData game : result.games()) {
            gameNumberTogameID.put(i, game.gameID());

            list += i + ". " + game.gameName() + "\n";
            if (game.whiteUsername() != null) { list += "White: " + game.whiteUsername() + "\n"; }
            if (game.blackUsername() != null) { list += "Black: " + game.blackUsername() + "\n"; }
            list += "\n";
        }
        return list;
    }

    public String playGame(String... params) throws ResponseException {
        // params = gameNumber, team
        if (params.length >= 2) {
            int gameNumber = Integer.parseInt(params[0]);
            int gameID = gameNumberTogameID.get(gameNumber);
            Model.JoinGameRequest request = new Model.JoinGameRequest(params[1], gameID);
            server.joinGame(request);

            return "Joined game " + gameNumber + " as " + params[1] + "\n";
        }
        throw new ResponseException(400, "Usage: playgame <game#> <Color: (White or Black)>\n");
    }

    public String observeGame(String... params) throws ResponseException {
        // params = gameNumber
        if (params.length >= 1) {
            int gameNumber = Integer.parseInt(params[0]);
            int gameID = gameNumberTogameID.get(gameNumber);

            return "Observing game " + gameNumber + "\n";
        }
        throw new ResponseException(400, "Usage: observegame <game#>\n");
    }


}
