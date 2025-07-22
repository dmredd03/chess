package dataaccess;
import model.Model;

import java.sql.SQLException;

interface UserDAO {
    Model.UserData getUser(String username) throws DataAccessException;
    void createUser(Model.UserData newUsername) throws DataAccessException;
    Boolean matchingPassword(Model.UserData user) throws DataAccessException;
    void clearUserDAO() throws DataAccessException, SQLException;
}

// Table:
// username, password, email - NOT NULL
// Connects to auth via username, since it must be unique to a user