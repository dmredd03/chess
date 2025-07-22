package dataaccess;

import model.Model;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.UUID;

public class AuthSQLDAO implements AuthDAO {

    public String createAuth(String username) throws DataAccessException {
        // if (authExists(username)) { throw new DataAccessException("authToken already exists"); }
        String newToken = UUID.randomUUID().toString();

        try (var conn = DatabaseManager.getConnection())  {
            var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, newToken);
            ps.setString(2, username);

            ps.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Already has authToken");
        }
        return newToken;
    }

    public Model.AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM authData WHERE authToken = ?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    return new Model.AuthData(authToken, username);
                } else {
                    throw new DataAccessException("authorization not found");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("authorization not found");
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM authData WHERE authToken = ?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);
            int deleted = ps.executeUpdate();
            if (deleted == 0) {
                throw new DataAccessException("Auth not found");
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Auth not found");
        }
    }

    public String getUserByAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM authData WHERE authToken = ?";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, authToken);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    throw new DataAccessException("authorization not found");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("authorization not found");
        }
    }

    String authTableCreation = """
            CREATE TABLE IF NOT EXISTS  authData (
            authToken varchar(256) NOT NULL PRIMARY KEY,
            username varchar(256) NOT NULL
            )
            """;

    public void clearAuthDAO() {
        try (var conn = DatabaseManager.getConnection()) {
            String drop = "TRUNCATE TABLE authData";
            try (var dropStatement = conn.prepareStatement(drop)) {
                dropStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            return ;
        }
    }

    private boolean authExists(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken FROM authData WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);

                try (var rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Connection error");
        }
    }
}
