package com.game;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


// versions.
// 1.1 add ability to shuffle cards up to one's wish instead of random
//
public class Application {

    static Client client1;
    static Client client2;
    static Client client3;

    static class MyFormatter extends Formatter {

        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append("[").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append("] - ");
            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }

        public String getHead(Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }

    public static void main(String[] args) throws IOException {

        try {
        FileHandler fileHandler = new FileHandler("log.txt");
        fileHandler.setLevel(Level.FINEST);
        fileHandler.setFormatter(new MyFormatter());
        Logger.getLogger("").addHandler(fileHandler);

        } catch (IOException e) {

        }


        gameManager = new GameManager();

        ConnectionManager connectionManager = new ConnectionManager(2, gameManager);
        connectionManager.setListener(gameManager);

        boolean test = true;
        if (test) {
            client1 = new Client();
            client2 = new Client();
            client3 = new Client();

            connectionManager.getListener().onClientConnected(client1);
            connectionManager.getListener().onClientConnected(client2);
            connectionManager.getListener().onClientConnected(client3);

            client1.receiveMessage("{\"event\":\"connect\",\"name\":\"player1\"}");
            client2.receiveMessage("{\"event\":\"connect\",\"name\":\"player2\"}");
            client3.receiveMessage("{\"event\":\"connect\",\"name\":\"player3\"}");
        } else {
            connectionManager.openForConnectionsAndWait();
        }
    }

    //TODO: no need for this var? only local
    private static GameManager gameManager;



}
