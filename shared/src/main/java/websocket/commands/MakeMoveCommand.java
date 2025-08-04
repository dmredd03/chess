package websocket.commands;
import chess.*;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final String color;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move, String color) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.color = color;
    }

    public ChessMove getMove() { return move; }

    public String getColor() { return color; }
}
