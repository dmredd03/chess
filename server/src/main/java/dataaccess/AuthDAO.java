package dataaccess;
import model.Model;

import java.sql.SQLException;

public interface AuthDAO {
    String createAuth(String username) throws DataAccessException;
    Model.AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    String getUserByAuth(String authToken) throws DataAccessException;
    void clearAuthDAO() throws DataAccessException, SQLException;
}

// Table: AuthKey, username, NOT NULL
// Primary Key: AuthKey, foreign Key: username
