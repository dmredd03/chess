package model;

import chess.ChessGame;

public class model {
    public record UserData(String username, String password, String email) {}
    public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}
    public record AuthData(String authToken, String username) {}

    // Register records
    public record RegisterResult(String username, String authToken) {} // TODO: remove
    public record RegisterRequest(String username, String password, String email) {}

    public record errorMessage(String message) {}
}
