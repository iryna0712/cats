package com.events.custom;

import com.entities.Card;
import com.events.EventJSON;

import java.io.Serializable;

public class ReceiveEventJSON extends EventJSON implements Serializable {
    private Card card;

    public ReceiveEventJSON(Card card) {
        super(EventJSON.EventJSONType.RECEIVE);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
//
//    public void setCard(CardType card) {
//        this.card = card;
//    }
}
