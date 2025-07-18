package dataaccess;

import model.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

public class UserSQLDAO implements UserDAO {

    public Model.UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public void createUser(Model.UserData newUsername) {

    }

    public Boolean matchingPassword(Model.UserData user) {
        return null;
    }

    public void clearUserDAO() {

    }

}
