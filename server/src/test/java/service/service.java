package service;// THIS IS FOR TEST
import dataaccess.DataAccessException;
import model.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;

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

    //Login tests
    @Test
    public void testLoginPositive() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "12345");
        model.LoginResult result = userService.login(loginReq);
        assertNotNull(result);
        assertEquals("david", result.username());
        assertEquals(result.authToken(), authDAO.getAuth(result.authToken()).authToken());
    }

    @Test
    public void testLoginNegative() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "wrongpassword");
        assertThrows(Exception.class, () -> {
            userService.login(loginReq);
        });
    }

    //Logout test
    @Test
    public void testLogoutPositive() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        model.RegisterResult regResult = userService.register(regReq);

        model.LogoutRequest req = new model.LogoutRequest(regResult.authToken());
        assertDoesNotThrow(() -> userService.logout(req));
    }

    @Test
    public void testLogoutNegative() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);

        assertThrows(DataAccessException.class, () -> {
            model.LogoutRequest req = new model.LogoutRequest("1234567890BadAuth");
            userService.logout(req);
        });

    }

    //List Game test

    @Test
    public void testListGamePositive() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "12345");
        model.LoginResult loginResult = userService.login(loginReq);

        model.CreateGameRequest gameReq = new model.CreateGameRequest("myFirstGame");
        model.CreateGameRequest gameReq2 = new model.CreateGameRequest("mySecondGame");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());

        assertDoesNotThrow(() -> {
            gameService.listGames(new model.ListGameRequest(authDAO.getAuth(loginResult.authToken()).authToken()));
        });
    }

    @Test
    public void testListGameNegative() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "12345");
        model.LoginResult loginResult = userService.login(loginReq);

        model.CreateGameRequest gameReq = new model.CreateGameRequest("myFirstGame");
        model.CreateGameRequest gameReq2 = new model.CreateGameRequest("mySecondGame");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());

        assertThrows(Exception.class, () -> {
            gameService.listGames(new model.ListGameRequest("Bad authorization"));
        });
    }

    // Create Game test

    @Test
    public void testCreateGamePositive() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "12345");
        model.LoginResult loginResult = userService.login(loginReq);

        model.CreateGameRequest gameReq = new model.CreateGameRequest("myFirstGame");
        model.CreateGameRequest gameReq2 = new model.CreateGameRequest("mySecondGame");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        assertDoesNotThrow(() -> {
            gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());
        });
    }

    @Test
    public void testCreateGameNegative() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "12345");
        model.LoginResult loginResult = userService.login(loginReq);

        model.CreateGameRequest gameReq = new model.CreateGameRequest("sameName");
        model.CreateGameRequest gameReq2 = new model.CreateGameRequest("sameName");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        assertThrows(Exception.class, () -> {
            gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());
        });
    }

    //Join Game test
    @Test
    public void joinGamePositive() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "12345");
        model.LoginResult loginResult = userService.login(loginReq);

        model.RegisterRequest regReq2 = new model.RegisterRequest("sam", "67890", "sam@email.com");
        userService.register(regReq2);
        model.LoginRequest loginReq2 = new model.LoginRequest("sam", "67890");
        model.LoginResult loginResult2 = userService.login(loginReq2);

        model.CreateGameRequest gameReq = new model.CreateGameRequest("All by myself");
        model.CreateGameRequest gameReq2 = new model.CreateGameRequest("With a friend :)");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        int gameID1 = gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();
        int gameID2 = gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();

        assertDoesNotThrow(() -> {
            model.JoinGameRequest joinReq01 = new model.JoinGameRequest("WHITE", gameID1);
            model.JoinGameRequest joinReq02 = new model.JoinGameRequest("BLACK", gameID1);
            gameService.joinGame(joinReq01, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq02, authDAO.getAuth(loginResult.authToken()).authToken());

            model.JoinGameRequest joinReq03 = new model.JoinGameRequest("WHITE", gameID2);
            model.JoinGameRequest joinReq04 = new model.JoinGameRequest("BLACK", gameID2);
            gameService.joinGame(joinReq03, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq04, authDAO.getAuth(loginResult2.authToken()).authToken());
        });
    }

    @Test
    public void joinGameNegative() throws Exception {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        model.RegisterRequest regReq = new model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        model.LoginRequest loginReq = new model.LoginRequest("david", "12345");
        model.LoginResult loginResult = userService.login(loginReq);

        model.RegisterRequest regReq2 = new model.RegisterRequest("sam", "67890", "sam@email.com");
        userService.register(regReq2);
        model.LoginRequest loginReq2 = new model.LoginRequest("sam", "67890");
        model.LoginResult loginResult2 = userService.login(loginReq2);

        model.CreateGameRequest gameReq = new model.CreateGameRequest("Bad playerColor");
        model.CreateGameRequest gameReq2 = new model.CreateGameRequest("Taking someone's place");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        int gameID1 = gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();
        int gameID2 = gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();
        assertThrows(Exception.class, () -> {
            model.JoinGameRequest joinReq01 = new model.JoinGameRequest("WHITE", gameID1);
            model.JoinGameRequest joinReq02 = new model.JoinGameRequest("PINK", gameID1);
            gameService.joinGame(joinReq01, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq02, authDAO.getAuth(loginResult.authToken()).authToken());
        });
        assertThrows(Exception.class, () -> {
            model.JoinGameRequest joinReq03 = new model.JoinGameRequest("WHITE", gameID2);
            model.JoinGameRequest joinReq04 = new model.JoinGameRequest("WHITE", gameID2);
            gameService.joinGame(joinReq03, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq04, authDAO.getAuth(loginResult2.authToken()).authToken());
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
