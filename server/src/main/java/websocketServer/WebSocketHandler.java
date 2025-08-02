package websocketServer;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import spark.Spark;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

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
            // notify players
        } catch ()
    }

    private void makeMove(UserGameCommand command) {

    }

    private void leave(UserGameCommand command) {

    }

    private void resign(UserGameCommand command) {

    }


}
