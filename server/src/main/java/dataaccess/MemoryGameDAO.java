package dataaccess;

import model.model;

import java.util.ArrayList;

// TODO: Implement serialization (creo que)
public class MemoryGameDAO implements GameDAO {
    private ArrayList<model.GameData> gameDb = new ArrayList<>();

    public void clear() {
        gameDb.clear();
    }

    public void createGame(model.GameData newGame) throws DataAccessException {
        for ( model.GameData game : gameDb ) {
            if ( game.gameID() == (newGame.gameID()) ) {
                throw new DataAccessException("Game already exists");
            }
        }
        gameDb.add(newGame);
    }

    public model.GameData getGame(int gameID) {
        for ( model.GameData game : gameDb ) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public ArrayList<model.GameData> listGame() {
        return gameDb;
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

}
