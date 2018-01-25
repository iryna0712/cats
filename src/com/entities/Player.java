package com.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class Player {

    private static LongAdder counter = new LongAdder();

    //TODO: do we need it?
    private short id;
    private String name;

    private List<Card> cards = new ArrayList<>();


    public Player(String name) {
        this.name = name;

        //TODO:is this atomic?
        id = counter.shortValue();
        counter.increment();
    }

    public List<Card> getCards() {
        return cards;
    }

    //ensure case of bomb? or is this managed by engine?
    public void receiveCard(Card card) {
        cards.add(card);
    }

    //maybe not needed
    public void onMakeMove() {

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(255);
        builder.append("Player:\n" +
                "id = " + id + "\n" +
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