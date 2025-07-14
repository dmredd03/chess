package dataaccess;

import chess.ChessGame;
import model.Model;
import service.AlreadyTaken;

import java.util.ArrayList;
import java.util.Random;


public class MemoryGameDAO implements GameDAO {
    private ArrayList<Model.GameData> gameDb = new ArrayList<>();

    public void clear() {
        gameDb.clear();
    }

    public int createGame(String gameName) throws DataAccessException {
        for ( Model.GameData game : gameDb ) {
            if ( game.gameName().equals(gameName)) {
                throw new DataAccessException("Game already exists");
            }
        }
        // Generate random 4-digit gameID given gameName for reproducibility
        boolean gameIDsearch = true;
        int iter = 0;
        int gameID = 0;
        while (gameIDsearch) {
            long seed = gameName.hashCode() + iter;
            Random rand = new Random(seed);
            gameID = 1000 + rand.nextInt(9000);

            boolean found = false;
            for ( Model.GameData game : gameDb ) {
                if (game.gameID() == gameID) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                gameIDsearch = false;
            } else {
                iter++;
            }
        }

        // Create game
        gameDb.add(new Model.GameData(gameID, null, null, gameName, new ChessGame()));

        return gameID;
    }

    public Model.GameData getGame(int gameID) {
        for ( Model.GameData game : gameDb ) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public ArrayList<Model.PrintGameData> listGame() {
        ArrayList<Model.PrintGameData> formattedGameList = new ArrayList<>();
        if (gameDb.isEmpty()) { return formattedGameList; }

        for (Model.GameData gameData : gameDb) {
            formattedGameList.add(formatGameData(gameData));
        }
        return formattedGameList;
    }

    public void updateGame(String playerColor, String username, int gameID) throws AlreadyTaken {

        for (Model.GameData game : gameDb) {
            if (game.gameID() == gameID) {
                switch (playerColor) {
                    case "WHITE":
                        if (game.whiteUsername() != null) { throw new AlreadyTaken("White player already taken"); }
                        Model.GameData newGameStateWhite = new Model.GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
                        gameDb.remove(game);
                        gameDb.add(newGameStateWhite);
                        break;
                    case "BLACK":
                        if (game.blackUsername() != null) { throw new AlreadyTaken("Black player already taken"); }
                        Model.GameData newGameStateBlack = new Model.GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
                        gameDb.remove(game);
                        gameDb.add(newGameStateBlack);
                        break;
                    default:
                        throw new AlreadyTaken("update failed");
                }
                break;
            }
        }
    }

    public void clearGameDAO() {
        if (!gameDb.isEmpty()) { gameDb.clear(); }
    }

    private Model.PrintGameData formatGameData(Model.GameData game) {
        return new Model.PrintGameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
    }

}
