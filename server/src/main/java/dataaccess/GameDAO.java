package dataaccess;
import model.model;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    int createGame(String gameName) throws DataAccessException;
    model.GameData getGame(int gameID);
    ArrayList<model.printGameData> listGame();
    void updateGame(model.GameData newGameState);
    void clearGameDAO();
}
