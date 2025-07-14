package dataaccess;

import chess.ChessGame;
import model.model;

import java.util.ArrayList;
import java.util.Random;

// TODO: Implement serialization (creo que)
public class MemoryGameDAO implements GameDAO {
    private ArrayList<model.GameData> gameDb = new ArrayList<>();

    public void clear() {
        gameDb.clear();
    }

    public int createGame(String gameName) throws DataAccessException {
        for ( model.GameData game : gameDb ) {
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
            for ( model.GameData game : gameDb ) {
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
        gameDb.add(new model.GameData(gameID, null, null, gameName, new ChessGame()));

        return gameID;
    }

    public model.GameData getGame(int gameID) {
        for ( model.GameData game : gameDb ) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public ArrayList<model.printGameData> listGame() {
        ArrayList<model.printGameData> formattedGameList = new ArrayList<>();
        if (gameDb.isEmpty()) return formattedGameList;

        for (model.GameData gameData : gameDb) {
            formattedGameList.add(formatGameData(gameData));
        }
        return formattedGameList;
    }

    public void updateGame(model.GameData newGameState) {
        for (model.GameData game : gameDb) {
            if (game.gameID() == newGameState.gameID()) {
                gameDb.remove(game);
                gameDb.add(newGameState);
                break;
            }
        }
    }

    public void clearGameDAO() {
        if (!gameDb.isEmpty()) gameDb.clear();
    }

    private model.printGameData formatGameData(model.GameData game) {
        return new model.printGameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
    }

}
