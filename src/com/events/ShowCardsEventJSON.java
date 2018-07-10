package com.events;

import com.entities.Card;
import com.entities.CardType;
import com.google.common.collect.Lists;

import java.util.List;

public class ShowCardsEventJSON extends EventJSON {
    public ShowCardsEventJSON() {
        super(EventJSONType.SHOW_CARDS);
    }

    private CardType[] cards;

    public void setCards(List<Card> cardsToShow) {
        cards = new CardType[cardsToShow.size()];
        List<CardType> cardTypeList = Lists.transform(cardsToShow, card -> card.getType());

        cards = cardTypeList.toArray(cards);
    }

    public CardType[] getCards() {
        return cards;
    }
}
