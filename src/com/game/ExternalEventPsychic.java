package com.game;

import com.events.EventJSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalEventPsychic implements ServerStreamListener {

    //private List<Client> clientsList = new ArrayList<>();
    public ExternalEventPsychic() {

    }

    //TODO: handle exceptions?
    @Override
    public void send(Client client, EventJSON event) {
        ObjectMapper mapper = new ObjectMapper();
        String message = null;
        try {
            message = mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            //TODO: log error
        }

        client.sendMessage(message);
    }
}
