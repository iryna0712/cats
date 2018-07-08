package com.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    //private static LongAdder counter = new LongAdder();

    private int clientId;
    private String name;
    private boolean active;
    private int num;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private List<Card> cards = new ArrayList<>();

    public Player(int clientId, String name) {
        this.name = name;
        this.clientId = clientId;
        //counter.increment();
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
        Collections.sort(cards);
        int result = Collections.binarySearch(cards,card);
        return (result >= 0);
    }

    //maybe not needed
    public void onMakeMove() {

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

    public Card pickOwnCard() {
        return null;
    }

    public Card pickOtherPlayerCard() {
        return null;
    }

    public Card takeCardFromDeck() {
        return null;
    }

}