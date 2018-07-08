package com.game;


import com.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class SampleClient extends Client {

    private static final Logger logger = Logger.getLogger(SampleClient.class.getSimpleName());

    public SampleClient(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void sendMessage(Message message) {
        System.out.println("______" + message + "______");
        //logger.severe("SampleClient, message to send:  " + message);
    }
}
