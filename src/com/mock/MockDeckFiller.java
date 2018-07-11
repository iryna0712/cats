package com.mock;

import com.entities.Card;
import com.entities.CardType;
import com.interfaces.DeckFiller;

import java.util.List;

public class MockDeckFiller implements DeckFiller {
    @Override
    public void fill(List<Card> cardsList) {
        cardsList.add(getCard(CardType.CUCUMBER));
        cardsList.add(getCard(CardType.SHUFFLE));
        cardsList.add(getCard(CardType.PLEASE));
        cardsList.add(getCard(CardType.SKIP));
        cardsList.add(getCard(CardType.FUTURE));
        cardsList.add(getCard(CardType.WATERMELON));
        cardsList.add(getCard(CardType.STOP));
        cardsList.add(getCard(CardType.SKIP));
        cardsList.add(getCard(CardType.CUCUMBER));

        cardsList.add(getCard(CardType.RAINBOW));
        cardsList.add(getCard(CardType.STOP));
        cardsList.add(getCard(CardType.SKIP));
        cardsList.add(getCard(CardType.SHAWERMA));
        cardsList.add(getCard(CardType.ATTACK));
        cardsList.add(getCard(CardType.BOMB));
        cardsList.add(getCard(CardType.STOP));
    }

    private Card getCard(CardType type) {
        return new Card(type);
    }
}
