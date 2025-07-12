package dataaccess;
import model.model;

interface UserDAO {
    model.UserData getUser(String username) throws DataAccessException;
    void createUser(model.UserData newUsername);
}

