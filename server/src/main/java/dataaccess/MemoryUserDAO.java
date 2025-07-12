package dataaccess;
import model.model;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private ArrayList<model.UserData> UserDb = new ArrayList<>();

    public model.UserData getUser(String username) throws DataAccessException {
        for (model.UserData user : UserDb) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(model.UserData newUser) {
        UserDb.add(newUser);
    }
}
