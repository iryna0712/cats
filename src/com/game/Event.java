package com.game;

import com.entities.Card;
import com.entities.Deck;
import com.entities.Player;

public class Event {

    public enum EventType {
        TAKE_CARD_FROM_DECK,
        PUT_CARD,
        GIVE_CARD,
        GIVE_UP
    }

    private int numPlayer;
    private EventType type;
    private Deck.Place place;

    public Deck.Place getPlace() {
        return place;
    }

    //TODO: maybe no setter for card? only in constructor

    //TODO: do not keep it here
    private Card card;

    public Event(Player player, EventType type) {
        //TODO: is id equal to number?
        numPlayer = player.getNum();
        this.type = type;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public EventType getType() {
        return type;
    }

    public Card getCard() {
        return card;
    }

    //TODO: maybe just card type is enough
    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "Event{" +
                "numPlayer=" + numPlayer +
                ", type=" + type +
                ", card=" + card +
                '}';
    }
}
