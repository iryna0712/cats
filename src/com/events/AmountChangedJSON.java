package com.events;

public class AmountChangedJSON extends EventJSON {
    public AmountChangedJSON(short playerId, int numOfCards) {
        super(EventJSONType.AMOUNT_CHANGED);
        this.id = playerId;
        this.numOfCards = numOfCards;
    }

    private short id;
    private int numOfCards;

    public short getId() {
        return id;
    }

    public int getNumOfCards() {
        return numOfCards;
    }
}
