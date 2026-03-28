package client;

import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade();
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        LoginRequest adminLogin = new LoginRequest("kythian", "andromeda");
        String adminAuth = facade.sendLoginRequest(adminLogin);
        //UserData adminRegister = new UserData("kythian", "kimball", "bassett");
        //String adminAuth2 = facade.sendRegisterRequest(adminRegister);
        facade.sendClearRequest(adminAuth);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sendRegisterRequestPositive() throws Exception {
        UserData registerRequest = new UserData("player1", "password", "email.com");
        String authToken = facade.sendRegisterRequest(registerRequest);
        assertTrue(authToken.length() > 10);
    }

    @Test
    public void sendRegisterRequestNegative() throws Exception {
        UserData registerRequest1 = new UserData("player1", "password", "email.com");
        String authToken1 = facade.sendRegisterRequest(registerRequest1);
        UserData registerRequest2 = new UserData("player1", "password2", "email2.com");
        String authToken2 = facade.sendRegisterRequest(registerRequest2);
        assertNull(authToken2);
    }

    @Test
    public void sendLoginRequestPositive() throws Exception {

    }

    @Test
    public void sendLoginRequestNegative() throws Exception {

    }

    @Test
    public void sendLogoutRequestPositive() throws Exception {

    }

    @Test
    public void sendLogoutRequestNegative() throws Exception {

    }

    @Test
    public void sendCreateGameRequestPositive() throws Exception {

    }

    @Test
    public void sendCreateGameRequestNegative() throws Exception {

    }

    @Test
    public void sendJoinGameRequestPositive() throws Exception {

    }

    @Test
    public void sendJoinGameRequestNegative() throws Exception {

    }

    @Test
    public void sendListGamesRequestPositive() throws Exception {

    }

    @Test
    public void sendListGamesRequestNegative() throws Exception {

    }

    @Test
    public void sendClearRequestPositive() throws Exception {

    }

    @Test
    public void sendClearRequestNegative() throws Exception {

    }
}
