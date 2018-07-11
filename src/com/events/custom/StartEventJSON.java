package com.events.custom;

import com.entities.Card;
import com.entities.Player;
import com.events.EventJSON;

import java.util.List;

public class StartEventJSON extends EventJSON {

    //@JsonProperty(value = "hand")
    private Card[] hand;
    //@JsonProperty(value = "table")
    private Player[] table;
    //@JsonProperty(value = "amountInDeck")
    private int amountInDeck;

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

    public Player[] getTable() {
        return table;
    }

    public void setTable(List<Player> players) {
        table = new Player[players.size()];
        table = players.toArray(table);
    }

    public int getAmountInDeck() {
        return amountInDeck;
    }
}
