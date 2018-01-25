package com.entities;

import java.util.Collections;
import java.util.LinkedList;

public class Deck {

    private final LinkedList<Card> deck = new LinkedList<>();

    public enum Place {
        TOP,
        BOTTOM,
        MIDDLE,
        RANDOM,
        SPECIFIC_INDEX;

        private static final int CLEARED_INDEX = -1;
        private int index = CLEARED_INDEX;

        public void Place() {}

        public void Place(int index) {
            this.index = index;
        }

        //must be private, this information should be known only to one player
        private int getIndex() {
            return index;
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

    public Card popTopCard() {
        return deck.pop();
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
            case SPECIFIC_INDEX:
                placeCardAtIndex(card, place.getIndex());
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
        deck.add(index, card);
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
