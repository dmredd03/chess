package dataaccess;
import dataaccess.*;
import model.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AlreadyTaken;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    private static DatabaseManager db;
    private UserSQLDAO userSQL;
    private AuthSQLDAO authSQL;
    private GameSQLDAO gameSQL;


    @BeforeAll
    public static void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();


    }

    String[] populateDataCommands = new String[]{
            "TRUNCATE TABLE userData",
            "TRUNCATE TABLE authData",
            "TRUNCATE TABLE gameData"
    };

    @BeforeEach
    public void populateData() throws DataAccessException {
        userSQL = new UserSQLDAO();
        authSQL = new AuthSQLDAO();
        gameSQL = new GameSQLDAO();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : populateDataCommands) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            var userAdd = "INSERT INTO userData (username, password, email) VALUES ('user', '123456', 'user@gmail.com')";
            var prepareUserAddition = conn.prepareStatement(userAdd);
            prepareUserAddition.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Unable to truncate", e);
        }
    }

    @AfterEach
    public void tearDown() throws DataAccessException {

    }

    // TESTS for USERSQLDAO
    // Create User tests
    @Test
    public void createUserPositive() throws Exception {
        Model.UserData testUser = new Model.UserData("david", "123456", "david@gmail.com");
        assertDoesNotThrow(() -> userSQL.createUser(testUser));
    }

    @Test
    public void createUserNegative() throws Exception {
        Model.UserData testUser = new Model.UserData("user", "123456", "email@gmail.com");
        assertThrows(DataAccessException.class, () -> userSQL.createUser(testUser));
    }

    // Get User tests
    @Test
    public void getUserPositive() throws Exception {
        assertNotNull(userSQL.getUser("user"));
    }

    @Test
    public void getUserNegative() throws Exception {
        assertNull(userSQL.getUser("I dont exist"));
    }

    // Matching password tests
    @Test
    public void matchingPasswordPositive() throws Exception {
        Model.UserData testUser = new Model.UserData("testUser", "12345678", "email");
        userSQL.createUser(testUser);

            assertTrue(() -> {
                try {
                    return userSQL.matchingPassword(testUser);
                } catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }
            });

    }

    @Test
    public void matchingPasswordNegative() throws Exception {
        Model.UserData testUser = new Model.UserData("testUser", "12345678", "email");
        userSQL.createUser(testUser);

        Model.UserData sameUserDiffPassword = new Model.UserData("testUser", "badPW", "email");
        assertFalse(() -> {
            try {
                return userSQL.matchingPassword(sameUserDiffPassword);
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // User clear test
    @Test
    public void userClearPositive() throws Exception {
        userSQL.clearUserDAO();
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement("SELECT COUNT(*) FROM userData");
            var rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count, "Table should be empty");
            } else {
                fail("not empty");
            }
        }
    }

    //AuthSQLDAO tests

    // CreateAuth tests
    @Test
    public void createAuthPositive() throws Exception {
        assertDoesNotThrow(() -> {
            authSQL.createAuth("newUser");
        });
    }

    @Test
    public void createAuthNegative() throws Exception {
        authSQL.createAuth("user");
        assertThrows(DataAccessException.class, () -> {
           authSQL.createAuth("user"); // line already exists
        });
    }

    // GetAuth tests
    @Test
    public void getAuthPositive() throws Exception {
        String myAuthToken = authSQL.createAuth("newUser");
        Model.AuthData testAuthData = new Model.AuthData(myAuthToken, "newUser");
        assertEquals(testAuthData, authSQL.getAuth(myAuthToken));
    }

    @Test
    public void getAuthNegative() throws Exception {
        String myAuthToken = authSQL.createAuth("newUser");
        assertThrows(DataAccessException.class, () -> {
           authSQL.getAuth(myAuthToken + "badAuth");
        });
    }

    // deleteAuth tests
    @Test
    public void deleteAuthPositive() throws Exception {
        String myAuthToken = authSQL.createAuth("newUser");
        assertDoesNotThrow(() -> authSQL.deleteAuth(myAuthToken));
    }

    @Test
    public void deleteAuthNegative() throws Exception {
        String myAuthToken = authSQL.createAuth("newUser");
        Model.AuthData testAuthData = authSQL.getAuth(myAuthToken);
        authSQL.deleteAuth(myAuthToken);
        assertThrows(DataAccessException.class, () -> authSQL.deleteAuth(myAuthToken));
    }

    // getUserByAuth tests
    @Test
    public void getUserByAuthPositive() throws Exception {
        String myAuthToken = authSQL.createAuth("newUser");
        Model.AuthData testAuthData = authSQL.getAuth(myAuthToken);
        assertDoesNotThrow(() -> authSQL.getUserByAuth(myAuthToken));
    }

    @Test
    public void getUserByAuthNegative() throws Exception {
        String myAuthToken = authSQL.createAuth("newUser");
        authSQL.deleteAuth(myAuthToken);
        assertThrows(DataAccessException.class, () -> authSQL.getUserByAuth(myAuthToken));
    }

    // Clear Auth DAO test
    @Test
    public void clearAuthDAOPositive() throws Exception {
        authSQL.createAuth("newUser");
        authSQL.createAuth("newUser2");
        authSQL.createAuth("newUser3");

        authSQL.clearAuthDAO();
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement("SELECT COUNT(*) FROM authData");
            var rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count, "Table should be empty");
            } else {
                fail("not empty");
            }
        }
    }

    // GameSQLDAO tests
    // createGame tests
    @Test
    public void createGamePositive() throws Exception {
        assertDoesNotThrow(() -> gameSQL.createGame("myFirstGame"));
    }

    @Test
    public void createGameNegative() throws Exception {
        gameSQL.createGame("myFirstGame");
        assertThrows(DataAccessException.class, () -> gameSQL.createGame("myFirstGame"));
    }

    // getGame tests
    @Test
    public void getGamePositive() throws Exception {
        int myFirstGameID = gameSQL.createGame("myFirstGame");
        assertNotNull(gameSQL.getGame(myFirstGameID));
    }

    @Test
    public void getGameNegative() throws Exception {
        int myFirstGameID = gameSQL.createGame("myFirstGame");
        assertNull(gameSQL.getGame(43110));
    }

    // listGameTests
    @Test
    public void listGamePositive() throws Exception {
        gameSQL.createGame("myFirstGame");
        gameSQL.createGame("mySecondGame");
        gameSQL.createGame("myThirdGame");

        assertDoesNotThrow(() -> gameSQL.listGame());
    }

    @Test
    public void listGameNegative() throws Exception {
        assertEquals(0, gameSQL.listGame().size());
    }

    // updateGame tests
    @Test
    public void updateGamePositive() throws Exception {
        int myFirstGameID = gameSQL.createGame("myFirstGame");
        gameSQL.createGame("mySecondGame");
        gameSQL.createGame("myThirdGame");

        assertDoesNotThrow(() -> gameSQL.updateGame("WHITE", "user", myFirstGameID));
        assertDoesNotThrow(() -> gameSQL.updateGame("BLACK", "user", myFirstGameID));
    }

    @Test
    public void updateGameNegative() throws Exception {
        int myFirstGameID = gameSQL.createGame("myFirstGame");
        gameSQL.createGame("mySecondGame");
        gameSQL.createGame("myThirdGame");

        assertDoesNotThrow(() -> gameSQL.updateGame("WHITE", "user", myFirstGameID));
        assertThrows(AlreadyTaken.class,() -> gameSQL.updateGame("WHITE", "user", myFirstGameID));
        assertThrows(AlreadyTaken.class, () -> gameSQL.updateGame("BLACK", "user", 12345));
    }

    // clearGameDAO test
    @Test
    public void clearGameDAOPositive() throws Exception {
        gameSQL.createGame("myFirstGame");
        gameSQL.createGame("mySecondGame");
        gameSQL.createGame("myThirdGame");

        gameSQL.clearGameDAO();

        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement("SELECT COUNT(*) FROM gameData");
            var rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count, "Table should be empty");
            } else {
                fail("not empty");
            }
        }
    }

}
