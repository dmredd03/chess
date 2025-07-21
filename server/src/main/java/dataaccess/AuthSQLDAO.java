package dataaccess;

import model.Model;

import java.sql.SQLException;

public class AuthSQLDAO implements AuthDAO {

    public String createAuth(String username) {
        return null;
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
            String drop = "DROP TABLE authData";
            try (var dropStatement = conn.prepareStatement(drop)) {
                var createStatement = conn.prepareStatement(authTableCreation);
                dropStatement.executeUpdate();
                createStatement.executeUpdate();
            }
        } catch (DataAccessException e) {
            return ;
        } catch (SQLException e) {
            return ;
        }
    }
}
