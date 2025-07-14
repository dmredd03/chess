package dataaccess;
import model.Model;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private ArrayList<Model.AuthData> authDb = new ArrayList<>();
    public String createAuth(String username) {
        String newToken = generateToken();
        Model.AuthData newAuth = new Model.AuthData(newToken, username);
        authDb.add(newAuth);
        return newToken;
    }

    public Model.AuthData getAuth(String authToken) throws DataAccessException {
        for ( Model.AuthData currAuthData : authDb ) {
            if (currAuthData.authToken().equals(authToken)) {
                return currAuthData;
            }
        }
        throw new DataAccessException("authorization not found");
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        for ( Model.AuthData currAuthData : authDb ) {
            if (currAuthData.authToken().equals(authToken)) {
                authDb.remove(currAuthData);
                return;
            }
        }
        throw new DataAccessException("authorization not found");
    }

    public String getUserByAuth(String authToken) throws DataAccessException {
        for ( Model.AuthData currAuthData : authDb ) {
            if (currAuthData.authToken().equals(authToken)) {
                return currAuthData.username();
            }
        }
        throw new DataAccessException("authorization not found");
    }

    public void clearAuthDAO() {
        if (!authDb.isEmpty()) authDb.clear();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }



}
