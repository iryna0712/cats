package com.entities;

public enum CardType {
    BOMB,
    SHIELD,
    PREDICTION,
    RECEIVE_PLAYERS_CARD,
    ATTACK,
    SKIP_MOVE,
    SHUFFLE,
    STOP,

    //paired cards
    SHAWERMA,
    WATERMELON,
    CUCUMBER,
    RAINBOW_CAT,
    LUMBERJACK;

    public static boolean isPairCard(CardType type) {
        return type.equals(SHAWERMA)||type.equals(WATERMELON)||
               type.equals(CUCUMBER)||type.equals(RAINBOW_CAT)||
                type.equals(LUMBERJACK);
    }

    public static boolean isCardStoppable(CardType type) {
        return type != BOMB && type != SHIELD;
    }
}
