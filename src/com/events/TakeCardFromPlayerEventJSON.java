package com.events;

import com.entities.CardType;

public class TakeCardFromPlayerEventJSON extends EventJSON {
    public TakeCardFromPlayerEventJSON(CardType card) {
        super(EventJSONType.TAKE);
        this.card = card;
    }

    private CardType card;

    public CardType getCard() {
        return card;
    }
}
