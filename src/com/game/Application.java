package com.game;

import com.entities.Card;
import com.entities.CardType;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;


// versions.
// 1.1 add ability to shuffle cards up to one's wish instead of random
//
public class Application {


    static Client client1;
    static Client client2;

    public static void main(String[] args) throws IOException {

        try {
        FileHandler fileHandler = new FileHandler("log.txt");
        fileHandler.setLevel(Level.FINEST);
        Logger.getLogger("").addHandler(fileHandler);
        } catch (IOException e) {

        }


        gameManager = new GameManager();

        ConnectionManager connectionManager = new ConnectionManager(2, gameManager);
        connectionManager.setListener(gameManager);

//        client1 = new Client(new Socket());
//        client2 = new Client(new Socket());
//
//        connectionManager.getListener().onClientConnected(client1);
//        connectionManager.getListener().onClientConnected(client2);
        connectionManager.openForConnectionsAndWait();
    }

    private static GameManager gameManager;



}
