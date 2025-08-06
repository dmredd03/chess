package websocket.commands;

public class ResignCommand extends UserGameCommand {
    private final String color;

    public ResignCommand(String authToken, Integer gameID, String color) {
        super(CommandType.RESIGN, authToken, gameID);
        this.color = color;
    }

    public String getColor() { return color; }
}
