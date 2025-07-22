package service;
import dataaccess.*;

import model.Model;

import java.sql.SQLException;


public class UserService {
    private UserSQLDAO userDAO;
    private AuthSQLDAO authDAO;
    private GameSQLDAO gameDAO;

    public UserService(UserSQLDAO userDAO, AuthSQLDAO authDAO, GameSQLDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Model.RegisterResult register(Model.RegisterRequest registerRequest) throws DataAccessException, SQLException {

        Model.UserData newUser = userDAO.getUser(registerRequest.username());
        if (newUser == null) {
            ; // person doesn't exist, continue
        } else {
            // throw an error
            throw new DataAccessException("User already exists");
        }

        userDAO.createUser(new Model.UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        String authKey = authDAO.createAuth(registerRequest.username());
        return new Model.RegisterResult(registerRequest.username(), authKey);
    }

    public Model.LoginResult login(Model.LoginRequest loginRequest) throws DataAccessException, SQLException {
        Model.UserData inputtedData = new Model.UserData(loginRequest.username(), loginRequest.password(), null);
        Model.UserData loginUser = userDAO.getUser(loginRequest.username());
        if (loginUser == null) {
            throw new DataAccessException("unauthorized");
        }
        // Check if password is correct
        if (!userDAO.matchingPassword(inputtedData)) { throw new DataAccessException("unauthorized"); }

        String myAuthToken = authDAO.createAuth(loginRequest.username());

        return new Model.LoginResult(loginUser.username(), myAuthToken);
    }

    public Model.LogoutResult logout(Model.LogoutRequest logoutRequest) throws DataAccessException, SQLException {
        Model.AuthData myAuthToken = authDAO.getAuth(logoutRequest.authorization()); // throws unauthorized exception if not found
        authDAO.deleteAuth(myAuthToken.authToken());
        return new Model.LogoutResult();
    }
}

