package dataaccess;

import model.Model;

import java.sql.SQLException;
import java.util.UUID;

public class AuthSQLDAO implements AuthDAO {

    public String createAuth(String username) {
        String newToken = UUID.randomUUID().toString();;

        try (var conn = DatabaseManager.getConnection())  {
            var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
            var ps = conn.prepareStatement(statement);
            ps.setString(1, newToken);
            ps.setString(2, username);

            ps.executeUpdate();
        } catch (SQLException e) {
            ;
        } catch (DataAccessException e) {
            ;
        }
        return newToken;
    }

    public Model.AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public String getUserByAuth(String authToken) throws DataAccessException {
        return null;
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
        } catch (DataAccessException e) {
            return ;
        } catch (SQLException e) {
            return ;
        }
    }
}
