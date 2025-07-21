package dataaccess;

import model.Model;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

public class UserSQLDAO implements UserDAO {

    public UserSQLDAO() {

    }

    public Model.UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM getUser WHERE getUser=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);

                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new Model.UserData(user, password, email);
                    } else {
                        throw new DataAccessException("User not found");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user", e);
        }
    }

    public void createUser(Model.UserData newUsername) {

    }

    public Boolean matchingPassword(Model.UserData user) {
        /*if (userDb.isEmpty()) { return false; }
        for (Model.UserData testUser : userDb) {
            if (testUser.password().equals(user.password()) && testUser.username().equals(user.username())) {
                return true;
            }
        }
        return false;
        return null;*/
        return false;
    }

    String userTable = """
                        CREATE TABLE IF NOT EXISTS  userData (
                        id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        username varchar(256) NOT NULL,
                        password varchar(256) NOT NULL,
                        email varchar(256) NOT NULL
                        )
                        """;
    public void clearUserDAO() {
        try (var conn = DatabaseManager.getConnection()) {
            String drop = "DROP TABLE userData";
            try (var dropStatement = conn.prepareStatement(drop)) {
                var createStatement = conn.prepareStatement(userTable);
                dropStatement.executeUpdate();
                createStatement.executeUpdate();
            }
        } catch (DataAccessException e) {
            return ;
        } catch (SQLException e) {
            return ;
        }
    }

    private void storeUserPassword(String usermane, String clearTextPassword) {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

}
