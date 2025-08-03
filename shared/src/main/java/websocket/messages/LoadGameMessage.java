package websocket.messages;
import chess.*;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;
    private String color;

    public LoadGameMessage(ServerMessage.ServerMessageType type, ChessGame game, String color) {
        super(type);
        this.game = game;
        this.color = color;
        // color = WHITE or BLACK for players, observer for observers
    }

    public ChessGame getChessGame() { return this.game; }

    public String getColor() { return this.color; }
}
