package websocketfacade;

import chess.*;
import com.google.gson.Gson;
import serverfacade.ResponseException;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException, URISyntaxException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();


            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {

                    notificationHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    // Add functions here
    public void connect(String authToken, int gameID, String color) throws ResponseException {
        try {
            var cmd = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID, String color) throws ResponseException {
        try {
            var cmd = new ConnectCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
            this.session.close();
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makemove(String authToken, Integer gameID, ChessMove move, String color) throws ResponseException {
        try {
            var cmd = new MakeMoveCommand(authToken, gameID, move, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws ResponseException {
        try {
            var cmd = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
