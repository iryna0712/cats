package com.events;

import com.entities.CardType;

import java.io.Serializable;

public class ReceiveEventJSON extends EventJSON implements Serializable {
    private CardType card;

    public ReceiveEventJSON(CardType card) {
        super(EventJSONType.RECEIVE);
        this.card = card;
    }

    public CardType getCard() {
        return card;
    }
//
//    public void setCard(CardType card) {
//        this.card = card;
//    }
}
