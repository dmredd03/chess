package dataaccess;
import model.Model;

import java.util.ArrayList;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;
    Model.GameData getGame(int gameID);
    ArrayList<Model.PrintGameData> listGame();
    void updateGame(String playerColor, String username, int gameID);
    void clearGameDAO();
}

// Table:
// gameID, game, gameName, NOT NULL & whiteUsername, blackUsername, can be null
// Primary Key: gameID
