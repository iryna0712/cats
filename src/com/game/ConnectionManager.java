package com.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Logger;

public class ConnectionManager {

    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getSimpleName());

    //todo: maybe we shouldnt expose this, because we have an inner class
    private int maxPlayers;
    //TODO: getter for long adder is expensive, should it be in while-loop
    private LongAdder currentNumPlayers = new LongAdder();
    private IConnectionListener listener;

    private List<Client> clientsList = new ArrayList<>();

    public static final int DEFAULT_PORT = 8080;

    private ServerSocket serverSocket;
    private int port = DEFAULT_PORT;


    public interface IConnectionListener {
        void onClientConnected(Client client);
        void onClientDisconnected(Client client);
    }

    public ConnectionManager(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    //should the connection manager be a separate thread??
    public ConnectionManager(int maxPlayers, IConnectionListener listener)  {
        this(maxPlayers);
        setListener(listener);
    }

    public final void setListener(IConnectionListener listener) {
        this.listener = listener;
    }

    //TODO: remove this
    public IConnectionListener getListener() {
        return listener;
    }

    public void openForConnectionsAndWait() throws IOException{
        try {
            if (port == 0) {
                serverSocket = new ServerSocket();
                port = serverSocket.getLocalPort();
            } else {
                serverSocket = new ServerSocket(port);
            }

            //listen for connections in a new thread
            new Thread(new ConnectionExecutor()).start();

        } catch (IOException e) {
            logger.severe("Exception during opening of server socket at port " + port + ".\nDetails: " + e);
            serverSocket.close();
        } finally {

        }
    }

    private class ConnectionExecutor implements Runnable {
        @Override
        public void run() {

                while (currentNumPlayers.intValue() < maxPlayers) {
                System.out.println("waiting");
                try {
                    //может ли заблокированный поток вывести из состояния обычное исключение?
                    Socket socket = serverSocket.accept();

                    try {
                        Client client = new Client(socket);
                        clientsList.add(client);
                        client.start();

                        getListener().onClientConnected(client);
                    } catch (IOException e) {

                    }

                } catch (IOException e) {
                    logger.severe("Exception during connection of client " + e);
                }
            }
        }
    }
}
