package websocketfacade;
import websocket.messages.ServerMessage;
public interface NotificationHandler {
    void notify(String notification);
}
