package com.bitriddler.tictactoe;

public class Main {
    public static void main(String[] args) {
        Arguments programArgs;

        System.out.println("\n" +
                "  _   _      _             _             \n" +
                " | | (_)    | |           | |            \n" +
                " | |_ _  ___| |_ __ _  ___| |_ ___   ___ \n" +
                " | __| |/ __| __/ _` |/ __| __/ _ \\ / _ \\\n" +
                " | |_| | (__| || (_| | (__| || (_) |  __/\n" +
                "  \\__|_|\\___|\\__\\__,_|\\___|\\__\\___/ \\___|\n" +
                "                                         \n" +
                "                                         \n");
        try {
            programArgs = Arguments.build(args);
            // Build configurations from file
            Runnable launcher = new ConsoleGameLauncher(
                    programArgs.getConfigurationFilename(),
                    programArgs.getPort()
            );
            new Thread(launcher).start();
        } catch (InvalidArgumentsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }
    }
}
