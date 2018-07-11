package com.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class Player {

    public static final short INVALID_ID = -1;
    private String name;
    private short externalId;
    private int clientId;

    private boolean active;
    private List<Card> cards = new ArrayList<>();

    private static LongAdder counter = new LongAdder();

    //TODO: externalId is required
    //TODO: check for max players and externalId value
    public Player(int clientId) {
        this.clientId = clientId;
        this.active = true;

        counter.increment();
        externalId = counter.shortValue();
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public short getExternalId() {
        return externalId;
    }

    public void setExternalId(short externalId) {
        this.externalId = externalId;
    }

    public int getClientId() {
        return clientId;
    }

    public List<Card> getCards() {
        return cards;
    }

    //ensure case of bomb? or is this managed by engine?
    public void receiveCard(Card card) {
        cards.add(card);
    }

    public boolean removeCard(Card card) {
        //TODO: quicker way to remove?
        return cards.remove(card);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean hasCard(CardType cardType) {
        return hasCard(cardType, 1);
    }

    public boolean hasCard(Card card) {
        return hasCard(card, 1);
    }

    public boolean hasCard(CardType cardType, int amount) {
        int frequency = Collections.frequency(cards, new Card(cardType));
        return (frequency == amount);
    }

    public boolean hasCard(Card card, int amount) {
        int frequency = Collections.frequency(cards, card);
        return (frequency == amount);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(255 );
        builder.append("Player: " +
                "name = " + name + " " +
                "cards = {");
        for (Card card : cards) {
            //TODO: no comma for last element;
            builder.append(card.getType().toString() + ", ");
        }

        //this is bad, change
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.deleteCharAt(builder.lastIndexOf(" "));
        builder.append("};");

        return builder.toString();
    }

    public void shuffleOwnCards() {
        Collections.shuffle(cards);
    }

    public Card pickOtherPlayerCard() {
        return null;
    }

    public Card takeCardFromDeck() {
        return null;
    }

}