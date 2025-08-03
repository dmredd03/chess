package websocketServer;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import spark.Spark;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketHandler {

    private final WebSocketConnManager connections = new WebSocketConnManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        var command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT:
                var connCommand = new Gson().fromJson(message, ConnectCommand.class);
                connect(connCommand, session);
            case UserGameCommand.CommandType.MAKE_MOVE:
                var makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(makeMoveCommand);
            case UserGameCommand.CommandType.LEAVE:
                leave(command);
            case UserGameCommand.CommandType.RESIGN:
                resign(command);
        }
    }

    private void connect(ConnectCommand command, Session session) throws IOException {
        try {
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            connections.add(username, session);
            ChessGame game = new dataaccess.GameSQLDAO().getGame(command.getGameID()).game();

            // notify
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, command.getColor());
            String notificationConnection;
            if (loadGameMessage.getColor().equals("observer")) {
                notificationConnection = String.format("%s is now observing", username);
            } else if (loadGameMessage.getColor().equals("WHITE")) {
                notificationConnection = String.format("%s joined game as white");
            } else {
                notificationConnection = String.format("%s joined game as black");
            }
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationConnection);
            connections.broadcast(username, message);
        } catch (DataAccessException | SQLException e) {
            throw new IOException("Error: unable to connect");
        }
    }

    private void makeMove(MakeMoveCommand command) {

    }

    private void leave(UserGameCommand command) {

    }

    private void resign(UserGameCommand command) {

    }


}
