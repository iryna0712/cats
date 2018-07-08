package com.game;

import com.entities.CardType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messages.Message;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client (Socket socket) throws IOException {
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
    }

    @Override
    public void run() {
        super.run();

        System.out.println("client run");
        //this is default implementation, we should read and send some data
        try {


            String str2 = "blabla";
            out.println(str2);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = null;
            try {
                jsonInString = mapper.writeValueAsString(new EventJSON(EventJSON.EventJSONType.RECEIVE, CardType.BOMB));
            } catch (Exception e) {

            }
            out.println(jsonInString);

            out.println("you lost");


            while (true) {
//                String str = in.readLine();
//                if (str.equals("END")) break;
//                System.out.println("Echoing: " + str);
//                out.println(str);

            }

//        } catch (IOException e) {
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

    public void sendMessage(Message message) {
        out.println(message);
    }
}
