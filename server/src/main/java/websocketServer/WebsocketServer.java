package websocketServer;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;

@WebSocket
public class WebsocketServer {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.webSocket("/ws", WebsocketServer.class);
        // Spark.get("/connect", (req, res));
        return desiredPort;
    }
}
