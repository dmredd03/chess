package ui;

import server.ServerFacade;

import java.util.Scanner;

public class PreloginUI {
    private final Client client;

    public PreloginUI(String serverUrl) {
        client = new Client(serverUrl);
    }

    public void run() {
        System.out.println("Welcome:");
        // print help code here

        Scanner scanner = new Scanner(System.in);
        var result = "";
        client.preloginEval("help");
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.preloginEval(line);
                System.out.print(result);
            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print("Enter command:");
    }
}
