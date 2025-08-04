package websocketServer;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameSQLDAO;
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
                break;
            case UserGameCommand.CommandType.MAKE_MOVE:
                var makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(makeMoveCommand);
                break;
            case UserGameCommand.CommandType.LEAVE:
                var exitCommand = new Gson().fromJson(message, ConnectCommand.class);
                leave(exitCommand);
                break;
            case UserGameCommand.CommandType.RESIGN:
                resign(command);
                break;
        }
    }

    private void connect(ConnectCommand command, Session session) throws IOException {
        try {
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            connections.add(command.getGameID(), username, command.getColor(), session);
            ChessGame game = new dataaccess.GameSQLDAO().getGame(command.getGameID()).game();

            // send loadgame message to user
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, command.getColor());
            connections.directBroadcast(command.getGameID(), username, command.getColor(), loadGameMessage);
            // notify other users
            String notificationConnection;
            if (command.getColor().equals("observer")) {
                notificationConnection = String.format("%s is now observing", username);
            } else if (command.getColor().equals("WHITE")) {
                notificationConnection = String.format("%s joined game as white", username);
            } else {
                notificationConnection = String.format("%s joined game as black", username);
            }
            var message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationConnection);
            connections.broadcast(command.getGameID(), username, command.getColor(), message);
        } catch (DataAccessException | SQLException e) {
            throw new IOException("Error: unable to connect");
        }
    }

    private void makeMove(MakeMoveCommand command) {

    }

    private void leave(ConnectCommand command) throws IOException {
        try {
            String username = new dataaccess.AuthSQLDAO().getUserByAuth(command.getAuthToken());
            String color = command.getColor();
            connections.remove(command.getGameID(), username, color);
            if (!color.equals("observer")) {
                new GameSQLDAO().removeUserFromGame(color, username, command.getGameID()); // should remove user from game
            }

            //notify
            String notificationLeave;
            if (command.getColor().equals("observer")) {
                notificationLeave = String.format("%s has stopped observing game", username);
            } else {
                notificationLeave = String.format("%s has stopped playing", username);
            }
            var newMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationLeave);
            connections.broadcast(command.getGameID(), username, color, newMessage);
        } catch (DataAccessException | SQLException e) {
            throw new IOException("Error: unable to leave game");
        }
    }

    private void resign(UserGameCommand command) {

    }


}
