package com.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {

    @JsonProperty(value = "card")
    private CardType type;
    @JsonIgnore
    private boolean isActive;

    //default constructor for JSON
    public Card() {}
    //TODO: more prettier way to get around this?
    public Card(String type) { this.type = CardType.fromJSONString(type);}
    public Card(CardType type) { this.type = type;}

    public void setType(CardType type) { this.type = type; }
    public CardType getType() {
        return type;
    }
    public void setType(String type) { this.type = CardType.fromJSONString(type); }

    public void setActive(boolean active) { isActive = active; }
    public boolean isActive() { return isActive; }

    public String toString() {
        return type.toString();
    }

    public void toggleActivity() {
        setActive(!isActive);
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(getType().ordinal(), o.getType().ordinal());
    }

    //TODO: override equals ans hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return ((Card) obj).getType().equals(this.getType());
    }

    public static Card fromType(CardType type) {
        return new Card(type);
    }


}
