package com.game;

public class StateManager {

    private GameState currentState;
    private int turn;
    private boolean isFirstMove = true;

    public StateManager() {
        currentState = GameState.GAME_START;
        turn = 0;
    }

    enum GameState {
        GAME_START,
        //when state waiting player, control is given to player
        WAITING_PLAYER,
        WAITING_PLAYER_DOUBLE_MOVE,
        WAITING_PLAYER_GIVE,
        WAITING_PLAYER_PICK,
        WAITING_BOMB_PLACEMENT,
        PICKING_CARD,
        BOMB_RECEIVED,
        EVENT_RECEIVED,
        POP_DECK,
        COOL_DOWN,
        GAME_OVER
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void switchTo(GameState state) {
        if (currentState.equals(GameState.GAME_START)) {
            if (state.equals(GameState.WAITING_PLAYER)) {
                toggleTurn();
                currentState = state;
                return;
            } else {
                //TODO: log error
                return;
            }
        } else {
            if (state.equals(GameState.WAITING_PLAYER_DOUBLE_MOVE)) {

            }
            currentState = state;
        }
    }

    public int getTurn() {
        return turn;
    }

    public void toggleTurn() {
        switch (turn) {
            case 0: turn = 1; break;
            case 1: turn = 2; break;
            case 2: turn = 1; break;
            default: break;
        }
    }
}
