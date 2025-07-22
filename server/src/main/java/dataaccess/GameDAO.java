package dataaccess;
import model.Model;
import service.AlreadyTaken;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException, SQLException;
    Model.GameData getGame(int gameID) throws SQLException;
    ArrayList<Model.PrintGameData> listGame() throws DataAccessException, SQLException;
    void updateGame(String playerColor, String username, int gameID) throws DataAccessException, AlreadyTaken, SQLException;
    void clearGameDAO() throws DataAccessException, SQLException;
}

// Table:
// gameID, game, gameName, NOT NULL & whiteUsername, blackUsername, can be null
// Primary Key: gameID
