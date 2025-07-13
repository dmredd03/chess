package dataaccess;
import model.model;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    void createGame(model.GameData newGame) throws DataAccessException;
    model.GameData getGame(int gameID);
    ArrayList<model.GameData> listGame();
    void updateGame(model.GameData newGameState);
    void clearGameDAO();
}
