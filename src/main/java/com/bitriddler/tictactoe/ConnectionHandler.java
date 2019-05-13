package com.bitriddler.tictactoe;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionHandler implements Runnable {
    Socket connectionSocket;

    public ConnectionHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        try {
            InputStream inputToServer = connectionSocket.getInputStream();
            OutputStream outputFromServer = connectionSocket.getOutputStream();

            Scanner scanner = new Scanner(inputToServer, "UTF-8");
            PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

            serverPrintOut.println("Hello World!");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                serverPrintOut.println("Echo back: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
