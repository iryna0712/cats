package com.entities;

import com.interfaces.DeckFiller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class DeckFillerImpl implements DeckFiller {

    private static final Logger logger = Logger.getLogger(DeckFiller.class.getSimpleName());

    private static final Map<CardType, Short> amountOfCards =
            Collections.unmodifiableMap( new HashMap<CardType, Short>() {{
                put(CardType.BOMB, (short)4);
                put(CardType.SHIELD, (short)6);
                put(CardType.FUTURE, (short)5);
                put(CardType.SHUFFLE, (short)4);
                put(CardType.STOP, (short)5);
                put(CardType.ATTACK, (short)4);
                put(CardType.SKIP, (short)4);
                put(CardType.PLEASE, (short)4);
                put(CardType.SHAWERMA, (short)4);
                put(CardType.WATERMELON, (short)4);
                put(CardType.CUCUMBER, (short)4);
                put(CardType.RAINBOW, (short)4);
                put(CardType.LUMBERJACK, (short)4);
            }});

    //TODO: deck filler should depend on the number of players
    public void fill(List<Card> cardsList) {
        cardsList.clear();

        for (CardType cardType: amountOfCards.keySet()) {

            Card card = new Card(cardType);
            short numberOfCardsForType = amountOfCards.get(cardType);

            cardsList.addAll(Collections.nCopies(numberOfCardsForType,card));
        }

        Collections.shuffle(cardsList);
        logger.severe("Filled card deck " + cardsList);
    }

}
