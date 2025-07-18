package dataaccess;

import model.Model;

public class AuthSQLDAO implements AuthDAO {

    public String createAuth(String username) {
        return null;
    }

    public Model.AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public String getUserByAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void clearAuthDAO() {

    }
}
