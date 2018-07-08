package com.entities;

import com.interfaces.DeckFiller;

import java.util.Collection;

public class MockDeckFiller implements DeckFiller {
    @Override
    public void fill(Collection<Card> cardsList) {
        cardsList.add(getCard(CardType.ATTACK));
        cardsList.add(getCard(CardType.PREDICTION));
        cardsList.add(getCard(CardType.WATERMELON));
        cardsList.add(getCard(CardType.CUCUMBER));
        cardsList.add(getCard(CardType.SHUFFLE));
        cardsList.add(getCard(CardType.RAINBOW_CAT));
        cardsList.add(getCard(CardType.CUCUMBER));
        cardsList.add(getCard(CardType.PREDICTION));
        cardsList.add(getCard(CardType.SHUFFLE));
        cardsList.add(getCard(CardType.RAINBOW_CAT));
        cardsList.add(getCard(CardType.SKIP_MOVE));
        cardsList.add(getCard(CardType.SHAWERMA));
        cardsList.add(getCard(CardType.ATTACK));
        cardsList.add(getCard(CardType.BOMB));
        cardsList.add(getCard(CardType.STOP));



    }

    private Card getCard(CardType type) {
        return new Card(type);
    }
}
