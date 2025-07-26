package ui;

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
}
