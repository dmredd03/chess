package dataaccess;
import dataaccess.*;
import model.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    private static DatabaseManager db;

    @BeforeAll
    public static void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {

    }

}
