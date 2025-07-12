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
}

