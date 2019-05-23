package com.bitriddler.tictactoe;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientSocketConnection {
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    ClientSocketConnection(Socket socket) {
        this.socket = socket;
        this.initializeIO();
    }

    private void initializeIO() {
        try {
            scanner = new Scanner(socket.getInputStream(), "UTF-8");
            writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),
                    true
            );
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    String input() {
        return scanner.nextLine();
    }

    void output(String str) {
        writer.println(str);
    }

    void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
