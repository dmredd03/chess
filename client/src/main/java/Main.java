import chess.*;

import ui.PreloginUI;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        System.out.println("♕ 240 Chess Client: ");

        new PreloginUI(serverUrl).run();
    }
}