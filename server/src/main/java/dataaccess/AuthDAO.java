package dataaccess;
import model.model;

public interface AuthDAO {
    void createAuth(String username);
    model.AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}
