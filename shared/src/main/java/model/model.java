package model;

import chess.ChessGame;

public class model {
    public record UserData(String username, String password, String email) {}
    public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}
    public record AuthData(String authToken, String username) {}

    // Register records
    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}

    // Login records
    public record LoginRequest(String username, String password) {}
    public record LoginResult(String username, String authToken) {}

    // Logut records
    public record LogoutRequest(String authorization) {}
    public record LogoutResult() {}

    // Clear records
    public record ClearRequest() {}
    public record ClearResult() {}

    public record errorMessage(String message) {}
}
