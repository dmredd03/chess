package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private String color;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, String color) {
        super(commandType, authToken, gameID);
        this.color = color;
    }

    public String getColor() { return this.color; }
}
