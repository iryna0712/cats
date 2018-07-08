package com.entities;

import com.interfaces.DeckFiller;

import java.util.*;
import java.util.logging.Logger;


public class DeckFillerImpl implements DeckFiller {

    private static final Logger logger = Logger.getLogger(DeckFiller.class.getSimpleName());

    private static final Map<CardType, Short> amountOfCards =
            Collections.unmodifiableMap( new HashMap<CardType, Short>() {{
                put(CardType.BOMB, (short)4);
                put(CardType.SHIELD, (short)6);
                put(CardType.PREDICTION, (short)5);
                put(CardType.SHUFFLE, (short)4);
                put(CardType.STOP, (short)5);
                put(CardType.ATTACK, (short)4);
                put(CardType.SKIP_MOVE, (short)4);
                put(CardType.RECEIVE_PLAYERS_CARD, (short)4);
                put(CardType.SHAWERMA, (short)4);
                put(CardType.WATERMELON, (short)4);
                put(CardType.CUCUMBER, (short)4);
                put(CardType.RAINBOW_CAT, (short)4);
                put(CardType.LUMBERJACK, (short)4);
            }});

    public void fill(Collection<Card> cardsList) {
        cardsList.clear();

        for (CardType cardType: amountOfCards.keySet()) {

            Card card = new Card(cardType);
            short numberOfCardsForType = amountOfCards.get(cardType);

            cardsList.addAll(Collections.nCopies(numberOfCardsForType,card));
        }

        logger.severe("Filled card deck " + cardsList);
    }

}
