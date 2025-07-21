package service;

import dataaccess.*;
import model.Model;

public class ClearService {
    private UserSQLDAO userDAO;
    private AuthSQLDAO authDAO;
    private GameSQLDAO gameDAO;

    public ClearService(UserSQLDAO userDAO, AuthSQLDAO authDAO, GameSQLDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Model.ClearResult clear() {
        userDAO.clearUserDAO();
        gameDAO.clearGameDAO();
        authDAO.clearAuthDAO();
        return new Model.ClearResult();
    }
}
