package ui;

import java.util.Scanner;

public class PostloginUI {
    private final Client client;

    public PostloginUI(Client client) {
        this.client = client;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        System.out.print(client.postloginEval("help"));
        System.out.println(client.postloginEval("listgame")); // sets numbers
        while (!result.equals("quit")) {
// print prompt here
            String line = scanner.nextLine();
            try {
                result = client.postloginEval(line);
                System.out.print(result);
            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print("Logged IN: ");
    }
}
