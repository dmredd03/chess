package websocketServer;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String username, Session session) {
        var connection = new Connection(username, session);
        connections
                .computeIfAbsent(gameID, k -> new ConcurrentHashMap<>())
                .put(username, connection);
    }

    public void remove(int gameID, String username) {
        ConcurrentHashMap<String, Connection> connectionGameSpecific = connections.get(gameID);
        if (connectionGameSpecific != null) {
            connectionGameSpecific.remove(username);
            if (connectionGameSpecific.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void broadcast(int gameID, String excludeUser, ServerMessage message) throws IOException {
        ConcurrentHashMap<String, Connection> connectionGameSpecific = connections.get(gameID);
        if (connectionGameSpecific == null) return;

        ArrayList<String> removeList = new ArrayList<>();
        for (var c : connectionGameSpecific.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUser)) {
                    c.send(new Gson().toJson(message));
                }
            } else {
                removeList.add(c.username);
            }
        }

        for (String username : removeList) {
            connectionGameSpecific.remove(username);
        }
        if (connectionGameSpecific.isEmpty()) {
            connections.remove(gameID);
        }
    }

}
