package dataaccess;
import model.Model;

public interface AuthDAO {
    String createAuth(String username);
    Model.AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    String getUserByAuth(String authToken) throws DataAccessException;
    void clearAuthDAO();
}

// Table: AuthKey, username, NOT NULL
// Primary Key: AuthKey, foreign Key: username
