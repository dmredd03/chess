package websocketserver;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class WebSocketConnManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<UserRole, Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String username, String color, Session session) {
        UserRole key = new UserRole(username, color);
        var connection = new Connection(username, session);
        connections
                .computeIfAbsent(gameID, k -> new ConcurrentHashMap<>())
                .put(key, connection);
    }

    public void remove(int gameID, String username, String color) {
        UserRole key = new UserRole(username, color);
        ConcurrentHashMap<UserRole, Connection> connectionGameSpecific = connections.get(gameID);
        if (connectionGameSpecific != null) {
            connectionGameSpecific.remove(key);
            if (connectionGameSpecific.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void broadcast(int gameID, String excludeUser, String excludeColor, ServerMessage message) throws IOException {
        ConcurrentHashMap<UserRole, Connection> connectionGameSpecific = connections.get(gameID);
        if (connectionGameSpecific == null) { return; }

        ArrayList<UserRole> removeList = new ArrayList<>();
        for (var entry : connectionGameSpecific.entrySet()) {
            UserRole key = entry.getKey();
            Connection c = entry.getValue();
            if (c.session.isOpen()) {
                if (!key.username.equals(excludeUser)) {
                    c.send(new Gson().toJson(message));
                }
            } else {
                removeList.add(key);
            }
        }

        for (UserRole key : removeList) {
            connectionGameSpecific.remove(key);
        }
        if (connectionGameSpecific.isEmpty()) {
            connections.remove(gameID);
        }
    }

    public void directBroadcast(int gameID, String username, String color, ServerMessage message) throws IOException {
        ConcurrentHashMap<UserRole, Connection> connectionGameSpecific = connections.get(gameID);
        if (connectionGameSpecific == null) { return; }
        UserRole key = new UserRole(username, color);
        Connection c = connectionGameSpecific.get(key);
        if (c != null && c.session.isOpen()) {
            c.send(new Gson().toJson(message));
        }
    }

}
