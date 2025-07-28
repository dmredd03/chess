package ui;

import model.Model;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

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
            return e.getMessage() + "\n";
        }
    }

    private final String helpPrelogin = "Commands:\n" +
                "Login to existing account: login <username> <password>\n" +
                "Register new account: register <username> <password> <email>\n" +
                "Quit Program: quit\n" +
                "Help for commands: help\n";

    public String login(String... params) throws ResponseException {
        // implement login code here
        if (params.length == 2) {
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
        if (params.length == 3) {
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
                case "listgame" -> listGame(params);
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
            "Play a game: playgame <game #> <Team Color (White or Black)>\n" +
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
            String gameNameInput = String.join(" ", params);

            Model.CreateGameRequest request = new Model.CreateGameRequest(gameNameInput);
            Model.CreateGameResult result = server.createGame(request);

            return "Created game " + gameNameInput + "\n";
        }
        throw new ResponseException(400, "Usage: creategame <game name>\n");
    }

    public String listGame(String... params) throws ResponseException {
        if (params.length >= 1) { throw new ResponseException(400, "Usage: listgame\n"); }

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
            i++;
        }

        if (gameNumberTogameID.isEmpty()) { return "No active games\n"; }

        return list;
    }

    public String playGame(String... params) throws ResponseException {
        // params = gameNumber, team
        if (params.length == 2) {
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Usage: playgame <game#> <Color: (White or Black)>\n");
            }
            int gameID;
            try {
                gameID = gameNumberTogameID.get(gameNumber);
            } catch (NullPointerException e) {
                throw new ResponseException(400, "Game does not exist\nUsage: playgame <game#> <Color: (White or Black)>\n");
            }
            String teamColor = params[1].trim().toUpperCase();
            if (!teamColor.equals("WHITE") && !teamColor.equals("BLACK")) {
                throw new ResponseException(400, "Team color must be WHITE or BLACK\n");
            }
            Model.JoinGameRequest request = new Model.JoinGameRequest(teamColor, gameID);
            server.joinGame(request);

            return "Joined game " + gameNumber + " as " + params[1] + "\n";
        }
        throw new ResponseException(400, "Usage: playgame <game#> <Color: (White or Black)>\n");
    }

    public String observeGame(String... params) throws ResponseException {
        // params = gameNumber
        if (params.length == 1) {
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Usage: observegame <game#>\n");
            }
            int gameID;
            try {
                gameID = gameNumberTogameID.get(gameNumber);
            } catch (NullPointerException e) {
                throw new ResponseException(400, "Game does not exist\nUsage: observegame <game#>\n");
            }

            return "Observing game " + gameNumber + "\n";
        }
        throw new ResponseException(400, "Usage: observegame <game#>\n");
    }


    public String gameplayEval(String input) {
        // try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                default -> "quit";
            };

    }


}
