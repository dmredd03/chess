package service;// THIS IS FOR TEST
import dataaccess.*;
import model.Model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ServiceTests {
    @Test
    public void testRegisterPositive() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        Model.RegisterRequest req = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(req);

        req = new Model.RegisterRequest("sam", "789", "sam@email.com");
        Model.RegisterResult result = userService.register(req);

        assertNotNull(result);
        assertEquals("sam", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void testRegisterNegative() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        Model.RegisterRequest req = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(req);

        assertThrows(Exception.class, ()  -> {
            userService.register(req);
        });
    }

    //Login tests
    @Test
    public void testLoginPositive() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "12345");
        Model.LoginResult result = userService.login(loginReq);
        assertNotNull(result);
        assertEquals("david", result.username());
        assertEquals(result.authToken(), authDAO.getAuth(result.authToken()).authToken());
    }

    @Test
    public void testLoginNegative() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);

        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "wrongpassword");
        assertThrows(Exception.class, () -> {
            userService.login(loginReq);
        });
    }

    //Logout test
    @Test
    public void testLogoutPositive() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        Model.RegisterResult regResult = userService.register(regReq);

        Model.LogoutRequest req = new Model.LogoutRequest(regResult.authToken());
        assertDoesNotThrow(() -> userService.logout(req));
    }

    @Test
    public void testLogoutNegative() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);

        assertThrows(DataAccessException.class, () -> {
            Model.LogoutRequest req = new Model.LogoutRequest("1234567890BadAuth");
            userService.logout(req);
        });

    }

    //List Game test

    @Test
    public void testListGamePositive() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "12345");
        Model.LoginResult loginResult = userService.login(loginReq);

        Model.CreateGameRequest gameReq = new Model.CreateGameRequest("myFirstGame");
        Model.CreateGameRequest gameReq2 = new Model.CreateGameRequest("mySecondGame");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());

        assertDoesNotThrow(() -> {
            gameService.listGames(new Model.ListGameRequest(authDAO.getAuth(loginResult.authToken()).authToken()));
        });
    }

    @Test
    public void testListGameNegative() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "12345");
        Model.LoginResult loginResult = userService.login(loginReq);

        Model.CreateGameRequest gameReq = new Model.CreateGameRequest("myFirstGame");
        Model.CreateGameRequest gameReq2 = new Model.CreateGameRequest("mySecondGame");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());

        assertThrows(Exception.class, () -> {
            gameService.listGames(new Model.ListGameRequest("Bad authorization"));
        });
    }

    // Create Game test

    @Test
    public void testCreateGamePositive() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "12345");
        Model.LoginResult loginResult = userService.login(loginReq);

        Model.CreateGameRequest gameReq = new Model.CreateGameRequest("myFirstGame");
        Model.CreateGameRequest gameReq2 = new Model.CreateGameRequest("mySecondGame");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        assertDoesNotThrow(() -> {
            gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());
        });
    }

    @Test
    public void testCreateGameNegative() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "12345");
        Model.LoginResult loginResult = userService.login(loginReq);

        Model.CreateGameRequest gameReq = new Model.CreateGameRequest("sameName");
        Model.CreateGameRequest gameReq2 = new Model.CreateGameRequest("sameName");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken());
        assertThrows(Exception.class, () -> {
            gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken());
        });
    }

    //Join Game test
    @Test
    public void joinGamePositive() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "12345");
        Model.LoginResult loginResult = userService.login(loginReq);

        Model.RegisterRequest regReq2 = new Model.RegisterRequest("sam", "67890", "sam@email.com");
        userService.register(regReq2);
        Model.LoginRequest loginReq2 = new Model.LoginRequest("sam", "67890");
        Model.LoginResult loginResult2 = userService.login(loginReq2);

        Model.CreateGameRequest gameReq = new Model.CreateGameRequest("All by myself");
        Model.CreateGameRequest gameReq2 = new Model.CreateGameRequest("With a friend :)");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        int gameID1 = gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();
        int gameID2 = gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();

        assertDoesNotThrow(() -> {
            Model.JoinGameRequest joinReq01 = new Model.JoinGameRequest("WHITE", gameID1);
            Model.JoinGameRequest joinReq02 = new Model.JoinGameRequest("BLACK", gameID1);
            gameService.joinGame(joinReq01, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq02, authDAO.getAuth(loginResult.authToken()).authToken());

            Model.JoinGameRequest joinReq03 = new Model.JoinGameRequest("WHITE", gameID2);
            Model.JoinGameRequest joinReq04 = new Model.JoinGameRequest("BLACK", gameID2);
            gameService.joinGame(joinReq03, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq04, authDAO.getAuth(loginResult2.authToken()).authToken());
        });
    }

    @Test
    public void joinGameNegative() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest regReq = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(regReq);
        Model.LoginRequest loginReq = new Model.LoginRequest("david", "12345");
        Model.LoginResult loginResult = userService.login(loginReq);

        Model.RegisterRequest regReq2 = new Model.RegisterRequest("sam", "67890", "sam@email.com");
        userService.register(regReq2);
        Model.LoginRequest loginReq2 = new Model.LoginRequest("sam", "67890");
        Model.LoginResult loginResult2 = userService.login(loginReq2);

        Model.CreateGameRequest gameReq = new Model.CreateGameRequest("Bad playerColor");
        Model.CreateGameRequest gameReq2 = new Model.CreateGameRequest("Taking someone's place");
        GameService gameService = new GameService(userDAO, authDAO, gameDAO);
        int gameID1 = gameService.createGame(gameReq, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();
        int gameID2 = gameService.createGame(gameReq2, authDAO.getAuth(loginResult.authToken()).authToken()).gameID();
        assertThrows(Exception.class, () -> {
            Model.JoinGameRequest joinReq01 = new Model.JoinGameRequest("WHITE", gameID1);
            Model.JoinGameRequest joinReq02 = new Model.JoinGameRequest("PINK", gameID1);
            gameService.joinGame(joinReq01, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq02, authDAO.getAuth(loginResult.authToken()).authToken());
        });
        assertThrows(Exception.class, () -> {
            Model.JoinGameRequest joinReq03 = new Model.JoinGameRequest("WHITE", gameID2);
            Model.JoinGameRequest joinReq04 = new Model.JoinGameRequest("WHITE", gameID2);
            gameService.joinGame(joinReq03, authDAO.getAuth(loginResult.authToken()).authToken());
            gameService.joinGame(joinReq04, authDAO.getAuth(loginResult2.authToken()).authToken());
        });
    }

    // User is added and then deleted, checks to make sure userDb is empty
    @Test
    public void clearPositive() throws Exception {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        UserService userService = new UserService(userDAO, authDAO, gameDAO);
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        Model.RegisterRequest req = new Model.RegisterRequest("david", "12345", "david@email.com");
        userService.register(req);
        clearService.clear();


        assertNull(userDAO.getUser("david"));
    }


}
