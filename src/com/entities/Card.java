package com.entities;

public class Card implements Comparable<Card> {

    private CardType type;

    public Card(CardType type) {
        this.type = type;
    }

    public CardType getType() {
        return type;
    }

    public String toString() {
        return type.toString();
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(getType().ordinal(), o.getType().ordinal());
    }

    //TODO: override equals ans hashCode
    @Override
    public boolean equals(Object obj) {
        //TODO: redo
        return ((Card) obj).getType().equals(this.getType());
    }
}
