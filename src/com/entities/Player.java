package com.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    //private static LongAdder counter = new LongAdder();
    private String name;
    private int num;
    private int clientId;

    private boolean active;
    private List<Card> cards = new ArrayList<>();

    public Player(int clientId) {
        this.clientId = clientId;
        //counter.increment();
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public boolean hasCard(Card card, int amount) {
        int frequency = Collections.frequency(cards, card);
        return (frequency == amount);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(255 );
        builder.append("Player:\n" +
                "name = " + name + "\n" +
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