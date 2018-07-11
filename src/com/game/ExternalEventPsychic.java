package com.game;

import com.entities.Player;
import com.events.EventJSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.Nullable;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ExternalEventPsychic implements ServerStreamListener {

    private static final Logger logger = Logger.getLogger(ExternalEventPsychic.class.getSimpleName());

    //TODO: psychic should maintain list of clients
    //when the client is kicked out, this should be managed somehow
    //private List<Client> clientsList = new ArrayList<>();

    private Map<Integer, Client> clients;
    private List<Player> players;

    public ExternalEventPsychic() {

    }
    public ExternalEventPsychic(Map<Integer, Client> clients, List<Player> players) {
        this.clients = clients;
        this.players = players;
    }

    public void setClients(Map<Integer, Client> clients) {
        this.clients = clients;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Nullable
    private Player getPlayer(Client client) {
        Player playerToReturn = null;
        for (Player player : players) {
            if (player.getClientId() == client.getUniqueId()) {
                playerToReturn = player;
                break;
            }
        }
        return playerToReturn;
    }

    //TODO: handle exceptions?
    @Override
    public void send(Client client, EventJSON event) {
        ObjectMapper mapper = new ObjectMapper();
        String message = null;
        try {
            message = mapper.writeValueAsString(event);

            //TODO: remove this
            if (players != null && clients != null) {
                Player player = getPlayer(client);
                logger.info("SEND to " + player + "\nMessage: " + message);
            }

        } catch (JsonProcessingException e) {
            //TODO: log error
        }

        client.sendMessage(message);
    }
}
