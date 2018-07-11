package com.events.custom;

import com.entities.Card;
import com.entities.Player;
import com.events.EventJSON;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class StartEventJSON extends EventJSON {

    private Card[] hand;
    private PlayerJSON[] table;
    private int amountInDeck;

    //TODO: serialize Player in a Custom way
    public class PlayerJSON implements Serializable {
        private String name;
        private int id;
        private int numOfCards;

        public PlayerJSON(String name, int id, int numOfCards) {
            this.name = name;
            this.id = id;
            this.numOfCards = numOfCards;
        }

        public PlayerJSON(Player player) {
            this.name = player.getName();
            this.id = player.getExternalId();
            this.numOfCards = player.getCards().size();
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public int getNumOfCards() {
            return numOfCards;
        }
    }

    public StartEventJSON(int amountInDeck) {
        super(EventJSONType.START);
        this.amountInDeck = amountInDeck;
    }

    public void setHand(List<Card> cards) {
        hand = new Card[cards.size()];
        //TODO: proper way to convert?
        hand = cards.toArray(hand);
    }

    public Card[] getHand() {
        return hand;
    }

    public PlayerJSON[] getTable() {
        return table;
    }

    public void setTable(List<Player> players) {
        table = new PlayerJSON[players.size()];
        List<PlayerJSON> playerList = Lists.transform(players, player -> new PlayerJSON(player));
        table = playerList.toArray(table);
    }

    public int getAmountInDeck() {
        return amountInDeck;
    }
}
