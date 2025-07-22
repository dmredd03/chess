package dataaccess;

import model.Model;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

public class UserSQLDAO implements UserDAO {

    public UserSQLDAO() {

    }

    public Model.UserData getUser(String username) throws DataAccessException, SQLException {

        try (var conn = DatabaseManager.getConnection()) {
            if (!userExists(username)) { return null; }
            var statement = "SELECT username, password, email FROM userData WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);

                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new Model.UserData(user, password, email);
                    } else {
                        throw new SQLException("User not found");
                    }
                }
            }
        }
    }

    public void createUser(Model.UserData newUsername) throws DataAccessException, SQLException {

        try (var conn = DatabaseManager.getConnection()) {
            if (userExists(newUsername.username())) { throw new DataAccessException("User already exists"); }
            var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
            var ps = conn.prepareStatement(statement);

            ps.setString(1, newUsername.username());
            ps.setString(2, hashPassword(newUsername.password()));
            ps.setString(3, newUsername.email());

            ps.executeUpdate();

        }
    }

    public Boolean matchingPassword(Model.UserData user) throws DataAccessException, SQLException {

        if (!userExists(user.username())) { throw new DataAccessException("User doesn't exists"); }

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password FROM userData WHERE username = ?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, user.username());
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPW = rs.getString("password");
                    return BCrypt.checkpw(user.password(), hashedPW);
                } else {
                    throw new DataAccessException("Access error");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Access error");
        }
    }



    String userTable = """
                        CREATE TABLE IF NOT EXISTS  userData (
                        username varchar(256) NOT NULL,
                        password varchar(256) NOT NULL,
                        email varchar(256) NOT NULL
                        )
                        """;
    public void clearUserDAO() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            String drop = "TRUNCATE TABLE userData";
            try (var dropStatement = conn.prepareStatement(drop)) {
                dropStatement.executeUpdate();
            }
        }
    }

    private String hashPassword(String clearTextPassword) throws DataAccessException {
        if (clearTextPassword == null ) { throw new DataAccessException("Password can't be null"); }
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    private boolean userExists (String username) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM userData WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);

                try (var rs = ps.executeQuery()) {
                    return rs.next(); //true, false
                }
            }
        }
    }

}
