package ui;

import com.google.gson.Gson;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocketfacade.NotificationHandler;

import java.util.Scanner;

public class PreloginUI implements NotificationHandler {
    private final Client client;

    public PreloginUI(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome:");
        // print help code here

        Scanner scanner = new Scanner(System.in);
        var result = "";
        System.out.print(client.preloginEval("help"));
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.preloginEval(line);
                System.out.print(result);
                if (result.contains("Successfully logged in") || result.contains("Successfully registered")) {
                    new PostloginUI(client).run();
                    System.out.print(client.preloginEval("help"));
                }
            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print("Logged OUT: ");
    }

    public void notify(String message) {
        var received = new Gson().fromJson(message, NotificationMessage.class);
        System.out.println(received.getNotification());
    }
}
