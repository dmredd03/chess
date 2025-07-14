package model;

import chess.ChessGame;

import java.util.ArrayList;

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

    // Logout records
    public record LogoutRequest(String authorization) {}
    public record LogoutResult() {}

    // List Games records
    public record printGameData(int gameID, String whiteUsername, String blackUsername, String gameName) {}
    public record ListGameRequest(String authorization) {}
    public record ListGameResult(ArrayList<printGameData> games) {}

    // Create Game records
    public record CreateGameRequest(String gameName) {}
    public record CreateGameResult(int gameID) {}

    // Join Game records
    public record JoinGameRequest(String playerColor, int gameID) {}
    public record JoinGameResult() {}

    // Clear records
    public record ClearRequest() {}
    public record ClearResult() {}

    public record errorMessage(String message) {}
}
