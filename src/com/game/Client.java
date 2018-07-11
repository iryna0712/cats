package com.game;

import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Logger;

public class Client extends Thread {

    private static final Logger logger = Logger.getLogger(Client.class.getSimpleName());

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private int uniqueId;

    private ClientStreamListener streamListener;

    public interface ClientStreamListener {
        void receive(Client client, String str);
    }

    //TODO: only for testing
    public Client() {
        this.uniqueId = super.hashCode();

    }

    public Client(Socket socket) throws IOException {
        //TODO: check this
        socket.setTcpNoDelay(true);
        System.out.println(socket);
        this.socket = socket;
        in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        // Enable auto-flush:
        out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream())), true);

        this.uniqueId = hashCode();
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setStreamListener(ClientStreamListener streamListener) {
        this.streamListener = streamListener;
    }

    @Override
    public void run() {
        super.run();

        try {
            while (true) {
                String str = in.readLine();
                receiveMessage(str);
            }
        } catch (IOException e) {
            //TODO: error
        } finally {
            try {
                System.out.println("closing...");
                socket.close();
            } catch(IOException e) {}
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, in, out);
    }

    public void sendMessage(@Nullable String message) {
        //logger.info("To client: " + this + "\nSend message: " + message);

        if (out != null && message != null) {
            out.println(message);
            out.flush();
            //TODO: why auto-flush not working?

        } else {
            //TODO: log error
        }

    }

    //TODO: public only for testing, make private again
    public void receiveMessage(String message) {
        //logger.info("From client: " + this + "\nReceived message: " + message);

        if (streamListener != null) {
            streamListener.receive(this, message);
        }
    }

    public void disconnect(boolean isForced) throws IOException {
        logger.info("Disconnecting client " + this + " isForced = " + isForced);
        if (socket != null) {
            socket.close();
        }

    }

    @Override
    public String toString() {
        return "Client{" +
                "socket=" + socket +
                ", uniqueId=" + uniqueId +
                '}';
    }
}
