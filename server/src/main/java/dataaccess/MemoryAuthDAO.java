package dataaccess;
import model.model;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private ArrayList<model.AuthData> authDb = new ArrayList<>();
    public String createAuth(String username) {
        String newToken = generateToken();
        model.AuthData newAuth = new model.AuthData(newToken, username);
        authDb.add(newAuth);
        return newToken;
    }

    public model.AuthData getAuth(String authToken) throws DataAccessException {
        for ( model.AuthData currAuthData : authDb ) {
            if (currAuthData.authToken().equals(authToken)) {
                return currAuthData;
            }
        }
        throw new DataAccessException("authorization not found");
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        for ( model.AuthData currAuthData : authDb ) {
            if (currAuthData.authToken().equals(authToken)) {
                authDb.remove(currAuthData);
                return;
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
