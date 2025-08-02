package websocketServer;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import spark.Spark;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final WebSocketConnManager connections = new WebSocketConnManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            command = new Gson().fromJson(message, MakeMoveCommand.class);
        }
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(command, session);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(command);
            case UserGameCommand.CommandType.LEAVE -> leave(command);
            case UserGameCommand.CommandType.RESIGN -> resign(command);
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        try {
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            connections.add(username, session);
            ChessGame game = new dataaccess.GameSQLDAO().getGame(command.getGameID()).game();
            // notify observers
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            connections.directBroadcast(username, loadGameMessage);
            String notificationConnection = String.format("%s is now observing", username);
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationConnection);
            connections.broadcast(username, message);
        } catch (Exception e) {

        }
    }

    private void makeMove(UserGameCommand command) {

    }

    private void leave(UserGameCommand command) {

    }

    private void resign(UserGameCommand command) {

    }


}
