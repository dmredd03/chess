package ui;

import java.util.Scanner;
import websocketfacade.NotificationHandler;

public class GameplayUI {
    public final Client client;
    private final PrintGameBoard gameboard = new PrintGameBoard();

    public GameplayUI(Client client) {
        this.client = client;

    }

    public void run(String playerColor, String inputResult) {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (true) {
            if (inputResult.contains("Joined")) {
                if (playerColor.equals("WHITE")) {
                    // print white board
                    gameboard.printBoardWhite();
                } else {
                    // print black board
                    gameboard.printBoardBlack();
                }
            } else {
                gameboard.printBoardWhite();
            }
            System.out.print("\n");


            String line = scanner.nextLine();
            try {
                result = client.gameplayEval(line);
                System.out.print(result);

                if (result.equals("quit")) {
                    break;
                }
            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }


}
