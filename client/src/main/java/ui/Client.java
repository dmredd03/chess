package ui;

import model.Model;
import server.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class Client {

    private final ServerFacade server;

    public Client(String serverUrl) {
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
                default -> help;
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private final String help = "Commands:\n" +
                "Login to existing account: login <username> <password>\n" +
                "Register new account: register <username> <password> <email>\n" +
                "Quit Program: quit" +
                "Help for commands: help";

    public String login(String... params) throws ResponseException {
        // implement login code here
        if (params.length >= 2) {
            Model.LoginRequest request = new Model.LoginRequest(params[0], params[1]);
            Model.LoginResult result = server.login(request);

            return "Successfully signed in as " + params[0];
        }
        throw new ResponseException(400, "Usage: login <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        // register code
        if (params.length >= 3) {
            Model.RegisterRequest request = new Model.RegisterRequest(params[0], params[1], params[2]);
            Model.RegisterResult result = server.register(request);

            return "Successfully registered as " + params[0];
        }
        throw new ResponseException(400, "Usage: register <username> <password> <email>");
    }
}
