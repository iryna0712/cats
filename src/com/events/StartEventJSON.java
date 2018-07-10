package com.events;

import com.entities.Card;
import com.entities.CardType;
import com.entities.Player;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class StartEventJSON extends EventJSON {

    //TODO: serialize Player in Custom way
    public class PlayerJSON implements Serializable {
        private String name;
        private int id;
        private int numOfCards;

        public PlayerJSON(String name, int id, int numOfCards) {
            this.name = name;
            this.id = id;
            this.numOfCards = numOfCards;
        }

        public PlayerJSON(Player player) {
            this.name = player.getName();
            this.id = player.getNum();
            this.numOfCards = player.getCards().size();
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public int getNumOfCards() {
            return numOfCards;
        }
    }

    private CardType[] hand;
    private PlayerJSON[] table;
    private int amountInDeck;


    public int getAmountInDeck() {
        return amountInDeck;
    }

    public StartEventJSON(int amountInDeck) {
        super(EventJSONType.START);
        this.amountInDeck = amountInDeck;
    }

    public void setHand(List<Card> cards) {
        hand = new CardType[cards.size()];
        List<CardType> cardTypeList = Lists.transform(cards, card -> card.getType());

        hand = cardTypeList.toArray(hand);
    }

    public CardType[] getHand() {
        return hand;
    }

    public PlayerJSON[] getTable() {
        return table;
    }

    public void setPlayers(List<Player> players) {
        table = new PlayerJSON[players.size()];
        List<PlayerJSON> playerList = Lists.transform(players, player -> new PlayerJSON(player));
        table = playerList.toArray(table);
    }
}
