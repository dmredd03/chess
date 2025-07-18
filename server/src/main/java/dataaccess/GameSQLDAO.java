package dataaccess;

import model.Model;

import java.util.ArrayList;

public class GameSQLDAO implements GameDAO {

    public void clear() {

    }

    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    public Model.GameData getGame(int gameID) {
        return null;
    }

    public ArrayList<Model.PrintGameData> listGame() {
        return null;
    }

    public void updateGame(String playerColor, String username, int gameID) {

    }

    public void clearGameDAO() {

    }


}

