package dataaccess;
import dataaccess.*;
import model.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


}
