package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.model;

import java.util.ArrayList;

public class GameService {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public GameService(MemoryUserDAO userDAO, MemoryAuthDAO authDAO, MemoryGameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public model.ListGameResult listGames(model.ListGameRequest listGameRequest) throws DataAccessException {
        model.AuthData myAuthData = authDAO.getAuth(listGameRequest.authorization());
        ArrayList<model.GameData> gameList = gameDAO.listGame();
        return new model.ListGameResult(gameList);
    }
}
