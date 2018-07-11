package com.events.custom;

import com.entities.Card;
import com.events.EventJSON;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayCardEventJSON extends EventJSON {
    @JsonProperty(value = "card")
    private Card card;
    //identifies to which player the card is played
    private short id;

    @JsonCreator
    public PlayCardEventJSON(@JsonProperty(value="card", required = true) Card card, @JsonProperty(value="id") short id) {
        super(EventJSONType.PLAY);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) { this.card = card; }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }
}
