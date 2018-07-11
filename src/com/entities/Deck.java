package com.entities;

import com.events.JSONConvertible;
import com.events.serializers.JSONConvertibleSerializer;
import com.events.serializers.PlaceJSONDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.interfaces.DeckFiller;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Deck {

    private static final Logger logger = Logger.getLogger(Deck.class.getSimpleName());

    private final LinkedList<Card> deck = new LinkedList<>();

    @JsonSerialize(using = JSONConvertibleSerializer.class)
    @JsonDeserialize(using = PlaceJSONDeserializer.class)
    public enum Place implements JSONConvertible {
        TOP("top"),
        BOTTOM("bottom"),
        MIDDLE("middle"),
        RANDOM("random"),
        INDEX("index");

        private static final int CLEARED_INDEX = -1;
        private int index = CLEARED_INDEX;

        public void Place() {}

        //public void Place(int index) {
//            this.index = index;
//        }

        Place(String jsonString) {
            this.jsonString = jsonString;
        }

        private String jsonString;


        public void setJsonString(String jsonString) {
            this.jsonString = jsonString;
        }

        @Override
        public String toJSONString() {
            return jsonString;
        }


        //must be private, this information should be known only to one player
        private int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        //
//        @Override
//        public String toJSONString() {
//            return toString().toLowerCase();
//        }

//        @Override
//        public JSONConvertible fromJSONString(String jsonString) {
//            return null;
//        }

        public static Place fromJSONString(String jsonString) {
            Place result = null;
            Place[] values = Place.values();
            for (Place value : values) {
                if (value.jsonString.equals(jsonString)) {
                    result = value;
                }
            }
            return result;
        }


    }

    public Deck(DeckFiller filler) {
        if (filler == null) {
            throw new IllegalArgumentException("Creating deck. Deck filler cannot be null!");
        }

        filler.fill(deck);
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card pollTopCard() {
        return deck.poll();
    }

    public Card popTopCard() throws IllegalStateException{
        if (deck.size() == 0) {
            //TODO: add properties file with strings for eceptions and messages
            throw new IllegalStateException("Something went wrong. Deck is empty. Cannot take card!");
        }
        return deck.pop();
    }

    public int getSize() {
        return deck.size();
    }

    @NotNull
    public List<Card> getTop3Cards() {
        //TODO: array lists with initial capacity everywhere
        List<Card> cards = new ArrayList<>(3);
        for (int i= 0; i < 3; i++) {
            Card card = deck.pop();
            cards.add(i, card);
        }
        //TODO: reverse order

        return cards;
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    //TODO: should be available only for bomb?
    //TODO: place card openly
    public void placeCardAt(Card card, Place place) {
        switch (place)   {
            case TOP:
                placeCardAtTop(card);
                break;
            case BOTTOM:
                placeCardAtTheBottom(card);
                break;
            case MIDDLE:
                placeCardInTheMiddle(card);
                break;
            case INDEX:
                placeCardAtIndex(card, place.getIndex());
                break;
            case RANDOM:
                addCardAndShuffle(card);
                break;

        }
    }

    protected void placeCardInTheMiddle(Card card) {
        int i = deck.size() / 2;
        placeCardAtIndex(card, i);
    }

    protected void placeCardAtTop(Card card) {
        deck.addFirst(card);
    }

    protected void placeCardAtTheBottom(Card card) {
        deck.addLast(card);
    }

    protected void placeCardAtIndex(Card card, int index) {
        if (index >= 0 && index < deck.size()) {
            deck.add(index, card);
        } else {
            logger.warning("Cannot place card at index : " + index +". The index is out of bounds");
        }
    }

    public void addCardAndShuffle(Card card) {
        deck.addLast(card);
        shuffle();
    }

    @Override
    public String toString() {
        return deck.toString();
    }
}
