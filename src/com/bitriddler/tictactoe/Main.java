package com.bitriddler.tictactoe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9991)) {
            while (true) {
                Socket connectionSocket = serverSocket.accept();
                Runnable connectionHandler = new ConnectionHandler(connectionSocket);
                new Thread(connectionHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
