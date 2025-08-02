package websocket.messages;
import chess.*;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;

    public LoadGameMessage(ServerMessage.ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getChessGame() { return this.game; }
}
