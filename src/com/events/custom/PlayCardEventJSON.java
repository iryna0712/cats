package com.events.custom;

import com.entities.Card;
import com.events.EventJSON;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"event","card","id"})
public class PlayCardEventJSON extends EventJSON {
    private Card card;
    //identifies to which player the card is played
    private short id;

    @JsonCreator
    public PlayCardEventJSON(@JsonProperty(value="card", required = true) Card card, @JsonProperty(value="id", defaultValue = "-1") short id) {
        super(EventJSONType.PLAY);
        this.card = card;
        this.id = id;
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
