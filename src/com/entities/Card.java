package com.entities;

public class Card {

    CardType type;

    public Card(CardType type) {
        this.type = type;
    }

    public CardType  getType() {
        return type;
    }

    public String toString() {
        return type.toString();
    }


}
