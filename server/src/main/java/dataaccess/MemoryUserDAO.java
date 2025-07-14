package dataaccess;
import model.Model;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private ArrayList<Model.UserData> userDb = new ArrayList<>();

    public Model.UserData getUser(String username) throws DataAccessException {
        for (Model.UserData user : userDb) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(Model.UserData newUser) {
        userDb.add(newUser);
    }

    public Boolean matchingPassword(Model.UserData user) {
        if (userDb.isEmpty()) { return false; }
        for (Model.UserData testUser : userDb) {
            if (testUser.password().equals(user.password()) && testUser.username().equals(user.username())) {
                return true;
            }
        }
        return false;
    }

    public void clearUserDAO() {
        if (!userDb.isEmpty()) { userDb.clear(); }
    }
}
