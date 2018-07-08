package com.events;

import com.entities.Card;
import com.entities.CardType;
import com.game.EventJSON;

import java.io.Serializable;
import java.util.List;

public class StartEventJSON extends EventJSON {

    public class PlayerJSON implements Serializable {
        private String name;
        private int id;
        private int numCards;

        public PlayerJSON(String name, int id, int numCards) {
            this.name = name;
            this.id = id;
            this.numCards = numCards;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public int getNumCards() {
            return numCards;
        }
    }

    private CardType[] hand;
    private PlayerJSON[] table;

    public StartEventJSON() {
        super(EventJSONType.START);
    }

    public void setHand(List<Card> cards) {
        Collection.tr

        for (Card card : cards) {
            hand[0]
        }
    }
}
