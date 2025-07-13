// THIS IS FOR TEST
import model.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import service.UserService;
import service.ClearService;

public class service {
    @Test
    public void testRegisterPositive() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        model.RegisterRequest req = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(req);

        req = new model.RegisterRequest("sam", "789", "sam@email.com");
        model.RegisterResult result = userService.register(req);

        assertNotNull(result);
        assertEquals("sam", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void testRegisterNegative() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        model.RegisterRequest req = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(req);

        assertThrows(Exception.class, ()  -> {
            userService.register(req);
        });

    }

    // User is added and then deleted, checks to make sure userDb is empty
    @Test
    public void clearPositive() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        model.RegisterRequest req = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(req);
        clearService.clear();


        assertNull(userDAO.getUser("david"));
    }


}
