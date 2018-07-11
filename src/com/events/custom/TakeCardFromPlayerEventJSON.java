package com.events.custom;

import com.entities.Card;
import com.events.EventJSON;

public class TakeCardFromPlayerEventJSON extends EventJSON {
    public TakeCardFromPlayerEventJSON(Card card) {
        super(EventJSON.EventJSONType.TAKE);
        this.card = card;
    }

    private Card card;

    public Card getCard() {
        return card;
    }
}
