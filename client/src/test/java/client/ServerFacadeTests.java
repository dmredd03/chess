package client;

import dataaccess.*;
import model.Model;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;
import service.ClearService;
import ui.*;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;



public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        String serverURL = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(serverURL);
    }

    String[] populateDataCommands = new String[]{
            "TRUNCATE TABLE userData",
            "TRUNCATE TABLE authData",
            "TRUNCATE TABLE gameData"
    };

    @BeforeEach
    public void clearSQL() throws DataAccessException, SQLException {
        UserSQLDAO userDAO = new UserSQLDAO();
        AuthSQLDAO authDAO = new AuthSQLDAO();
        GameSQLDAO gameDAO = new GameSQLDAO();
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        clearService.clear();
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws Exception {
        var authData = serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void registerNegative() throws Exception {
        serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        // duplicate register request, should throw

        assertThrows(Exception.class, () -> serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email")));
    }

    @Test
    public void loginPositive() throws Exception {
        serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        assertDoesNotThrow(() -> serverFacade.login(new Model.LoginRequest("player1", "password")));
    }

    @Test
    public void loginNegative() throws Exception {
        // user does not exist
        assertThrows(Exception.class, () -> serverFacade.login(new Model.LoginRequest("player1", "password")));
    }

    @Test
    public void logoutPositive() throws Exception {
        var user = serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.setAuthorization(user.authToken());
        System.out.print(user.authToken());
        assertNotNull(user, "Register returned null user");
        assertNotNull(user.authToken(), "Registered returned null auth token");
        assertDoesNotThrow(() -> serverFacade.logout(new Model.LogoutRequest(serverFacade.getAuthorization())));

    }

    @Test
    public void logoutNegative() throws Exception {
        serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.login(new Model.LoginRequest("player1", "password"));
        assertThrows(Exception.class, () -> serverFacade.logout(new Model.LogoutRequest("badAuthToken")));
    }

    @Test
    public void listGamePositive() throws Exception {
        var user = serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.setAuthorization(user.authToken());
        assertDoesNotThrow(() -> serverFacade.listGame(new Model.ListGameRequest(serverFacade.getAuthorization())));
    }

    @Test
    public void listGameNegative() throws Exception {
        serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.setAuthorization("badAuth");
        assertThrows(Exception.class, () -> serverFacade.listGame(new Model.ListGameRequest("badAuth")));
    }

    @Test
    public void createGamePositive() throws Exception {
        var user = serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.setAuthorization(user.authToken());
        assertDoesNotThrow(() -> serverFacade.createGame(new Model.CreateGameRequest("MyFirstGame")));
    }

    @Test
    public void createGameNegative() throws Exception {
        serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.setAuthorization("BadAuth");
        assertThrows(Exception.class, () -> serverFacade.createGame(new Model.CreateGameRequest("MyFirstGame")));
    }

    @Test
    public void joinGamePositive() throws Exception {
        var user = serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.setAuthorization(user.authToken());
        var game = serverFacade.createGame(new Model.CreateGameRequest("MyFirstGame"));
        assertDoesNotThrow(() -> serverFacade.joinGame(new Model.JoinGameRequest("WHITE", game.gameID())));
    }

    @Test
    public void joinGameNegative() throws Exception {
        var user = serverFacade.register(new Model.RegisterRequest(
                "player1", "password", "email"));
        serverFacade.setAuthorization(user.authToken());
        serverFacade.createGame(new Model.CreateGameRequest("MyFirstGame"));
        assertThrows(Exception.class, () -> serverFacade.joinGame(new Model.JoinGameRequest("WHITE", 1111)));
    }

}
