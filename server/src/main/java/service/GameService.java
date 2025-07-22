package service;

import dataaccess.*;
import model.Model;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameService {
    private UserSQLDAO userDAO;
    private AuthSQLDAO authDAO;
    private GameSQLDAO gameDAO;

    public GameService(UserSQLDAO userDAO, AuthSQLDAO authDAO, GameSQLDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Model.ListGameResult listGames(Model.ListGameRequest listGameRequest) throws DataAccessException, SQLException {
        Model.AuthData myAuthData = authDAO.getAuth(listGameRequest.authorization());
        ArrayList<Model.PrintGameData> gameList = gameDAO.listGame();
        return new Model.ListGameResult(gameList);
    }

    public Model.CreateGameResult createGame(Model.CreateGameRequest createRequest, String authorization) throws DataAccessException, SQLException {
        Model.AuthData myAuthData = authDAO.getAuth(authorization);
        int newGameID = gameDAO.createGame(createRequest.gameName());
        return new Model.CreateGameResult(newGameID);
    }

    public Model.JoinGameResult joinGame(Model.JoinGameRequest joinRequest, String authorization) throws DataAccessException, AlreadyTaken, SQLException {
        Model.AuthData myAuthData = authDAO.getAuth(authorization);
        Model.GameData myGame = gameDAO.getGame(joinRequest.gameID());
        String myUsername = authDAO.getUserByAuth(authorization);
        gameDAO.updateGame(joinRequest.playerColor(), myUsername, joinRequest.gameID());
        return new Model.JoinGameResult();
    }
}
