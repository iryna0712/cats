package com.events;

import com.entities.CardType;

public class PlayCardEventJSON extends EventJSON {
    private CardType card;
    //TODO: id short or int; do we need id? probably not
    //identifies to which player is played
    //TODO: make not required
    private int id;

    //constructor for deserialization
    public PlayCardEventJSON() {}

    public PlayCardEventJSON(CardType card) {
        super(EventJSONType.PLAY);
        this.card = card;
    }

    public CardType getCard() {
        return card;
    }

    public void setCard(CardType card) { this.card = card; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
