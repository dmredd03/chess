package dataaccess;
import model.Model;

import java.sql.SQLException;

public interface AuthDAO {
    String createAuth(String username) throws DataAccessException, SQLException;
    Model.AuthData getAuth(String authToken) throws DataAccessException, SQLException;
    void deleteAuth(String authToken) throws DataAccessException, SQLException;
    String getUserByAuth(String authToken) throws DataAccessException, SQLException;
    void clearAuthDAO() throws DataAccessException, SQLException;
}

// Table: AuthKey, username, NOT NULL
// Primary Key: AuthKey, foreign Key: username
