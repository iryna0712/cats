package com.events.custom;

import com.entities.Card;
import com.events.EventJSON;

import java.util.List;

public class ShowCardsEventJSON extends EventJSON {
    public ShowCardsEventJSON() {
        super(EventJSON.EventJSONType.SHOW_CARDS);
    }

    private Card[] cards;

    public void setCards(List<Card> cardsToShow) {
        cards = new Card[cardsToShow.size()];
        cards = cardsToShow.toArray(cards);
    }

    public Card[] getCards() {
        return cards;
    }
}
