package dataaccess;
import model.Model;

interface UserDAO {
    Model.UserData getUser(String username) throws DataAccessException;
    void createUser(Model.UserData newUsername);
    Boolean matchingPassword(Model.UserData user);
    void clearUserDAO();
}

