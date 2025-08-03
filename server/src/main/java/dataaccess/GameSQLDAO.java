package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Model;
import service.AlreadyTaken;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class GameSQLDAO implements GameDAO {

    public int createGame(String gameName) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO gameData (gameID, gameName, game) VALUES (?, ?, ?)";
            var ps = conn.prepareStatement(statement);
            int newGameID = generateGameID(gameName);
            ps.setInt(1, newGameID);
            ps.setString(2, gameName);
            ChessGame defaultGame = new ChessGame();
            String gsonGame = new Gson().toJson(defaultGame);
            ps.setString(3, gsonGame);

            ps.executeUpdate();
            return newGameID;
        }
    }

    private int generateGameID(String gameName) {
        // Generate random 4-digit gameID given gameName for reproducibility
        int iter = 0;
        int gameID = 0;

        long seed = gameName.hashCode() + iter;
        Random rand = new Random(seed);
        gameID = 1000 + rand.nextInt(9000);
        // add code to check if gameID repeats
        return gameID;
    }

    public Model.GameData getGame(int gameID) throws SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameID = ?";
            var ps = conn.prepareStatement(statement);
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readGameData(gameID, rs);
                } else {
                    return null; // not found
                }
            }
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Model.GameData readGameData(int gameID, ResultSet rs) throws SQLException {
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String game = rs.getString("game");
        ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
        return new Model.GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
    }

    public ArrayList<Model.PrintGameData> listGame() throws DataAccessException, SQLException {
        ArrayList<Model.PrintGameData> formattedGameList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData";
            var ps = conn.prepareStatement(statement);
            var rs = ps.executeQuery();
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                Model.GameData rowData = readGameData(gameID, rs);

                formattedGameList.add(new Model.PrintGameData(gameID,
                        rowData.whiteUsername(),
                        rowData.blackUsername(),
                        rowData.gameName()));
            }

            return formattedGameList;
        }
    }

    public void updateGame(String playerColor, String username, int gameID) throws DataAccessException, AlreadyTaken, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT whiteUsername, blackUsername FROM gameData WHERE gameID = ?";
            var ps = conn.prepareStatement(statement);
            ps.setInt(1, gameID);
            try ( var rs = ps.executeQuery() ) {
                if (!rs.next()) {
                    throw new DataAccessException("Game not found");
                }


                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                var newStatement = "UPDATE gameData"; // placeholder
                switch (playerColor) {
                    case "WHITE":
                        if (whiteUsername != null) { throw new AlreadyTaken("White player already taken"); }
                        newStatement = "UPDATE gameData SET whiteUsername = ? WHERE gameID = ?";

                        break;
                        case "BLACK":
                            if (blackUsername != null) { throw new AlreadyTaken("Black player already taken"); }
                            newStatement = "UPDATE gameData SET blackUsername = ? WHERE gameID = ?";

                            break;
                        default:
                            throw new AlreadyTaken("update failed");
                    }

                    try (var newPs = conn.prepareStatement(newStatement)) {
                        newPs.setString(1, username);
                        newPs.setInt(2, gameID);
                        newPs.executeUpdate();
                    }

            }

        }
    }


    public void removeUserFromGame(String playerColor, String username, int gameID) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT whiteUsername, blackUsername FROM gameData WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("Game not found");
                    }
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");

                    String updateSql = null;
                    switch (playerColor.toUpperCase()) {
                        case "WHITE":
                            if (username.equals(whiteUsername)) {
                                updateSql = "UPDATE gameData SET whiteUsername = NULL WHERE gameID = ?";
                            }
                            break;
                        case "BLACK":
                            if (username.equals(blackUsername)) {
                                updateSql = "UPDATE gameData SET blackUsername = NULL WHERE gameID = ?";
                            }
                            break;
                    }
                    if (updateSql != null) {
                        try (var ups = conn.prepareStatement(updateSql)) {
                            ups.setInt(1, gameID);
                            ups.executeUpdate();
                        }
                    }
                }
            }
        }
    }


    String gameTableCreation = """
            CREATE TABLE IF NOT EXISTS  gameData (
            gameID int NOT NULL PRIMARY KEY,
            whiteUsername varchar(256) DEFAULT NULL,
            blackUsername varchar(256) DEFAULT NULL,
            gameName varchar(256) NOT NULL,
            game TEXT NOT NULL
            )
            """;

    public void clearGameDAO() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            String drop = "TRUNCATE TABLE gameData";
            try (var dropStatement = conn.prepareStatement(drop)) {
                dropStatement.executeUpdate();
            }
        }
    }


}

