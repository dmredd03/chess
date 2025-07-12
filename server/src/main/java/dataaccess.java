import java.util.ArrayList;

public class dataaccess {
    private ArrayList<model.UserData> UserData = new ArrayList<>();
    private ArrayList<model.AuthData> AuthData = new ArrayList<>();
    private ArrayList<model.GameData> GameData = new ArrayList<>();


    /*public model.UserData getUser(String username) throws DataAccessException {
        for (model.UserData user : UserData) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return
    }*/
}

interface UserDAO {


    model.UserData getUser(String username);
}


