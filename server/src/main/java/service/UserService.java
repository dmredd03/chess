package service;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

import model.model;
import spark.Request;
import spark.Response;



public class UserService {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public UserService(MemoryUserDAO userDAO, MemoryAuthDAO authDAO, MemoryGameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public model.RegisterResult register(model.RegisterRequest registerRequest) throws DataAccessException {

        model.UserData newUser = userDAO.getUser(registerRequest.username());
        if (newUser == null) {
            ; // person doesn't exist, continue
        } else {
            // throw an error
            throw new DataAccessException("User already exists");
        }

        userDAO.createUser(new model.UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        String authKey = authDAO.createAuth(registerRequest.username());
        return new model.RegisterResult(registerRequest.username(), authKey);
    }

    public model.LoginResult login(model.LoginRequest loginRequest) throws DataAccessException {
        model.UserData inputtedData = new model.UserData(loginRequest.username(), loginRequest.password(), null);
        model.UserData loginUser = userDAO.getUser(loginRequest.username());
        if (loginUser == null) {
            throw new DataAccessException("unauthorized");
        }
        // Check if password is correct
        if (!userDAO.matchingPassword(inputtedData)) throw new DataAccessException("unauthorized");

        String myAuthToken = authDAO.createAuth(loginRequest.username());

        return new model.LoginResult(loginUser.username(), myAuthToken);
    }

    public model.LogoutResult logout(model.LogoutRequest logoutRequest) throws DataAccessException {
        model.AuthData myAuthToken = authDAO.getAuth(logoutRequest.authorization()); // throws unauthorized exception if not found
        authDAO.deleteAuth(myAuthToken.authToken());
        return new model.LogoutResult();
    }
}

