package com.bitriddler.tictactoe;

import com.bitriddler.tictactoe.game.GameIO;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientSocketConnection implements GameIO {
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

    @Override
    public String input() {
        return scanner.nextLine();
    }

    @Override
    public void output(String str) {
        writer.println(str);
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
