package com.game;

public class StateManager {

    private GameState currentState;
    private short turn;
    private TurnChangeListener turnChangeListener;

    public StateManager() {
        currentState = GameState.GAME_START;
        turn = 0;
    }

    public void setTurnChangeListener(TurnChangeListener turnChangeListener) {
        this.turnChangeListener = turnChangeListener;
    }

    enum GameState {
        GAME_START,
        //when state waiting player, control is given to player
        WAITING_PLAYER,
        WAITING_PLAYER_DOUBLE_MOVE,
        WAITING_PLAYER_GIVE,
        WAITING_PLAYER_PICK,
        PICKING_CARD,
        BOMB_RECEIVED,
        EVENT_RECEIVED,
        COOL_DOWN,
        GAME_OVER
    }

    public GameState getCurrentState() {
        return currentState;
    }

    //TODO:do we have to confirm valid transitions?
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

    private short lastPlayer;
    //TODO: maybe private
    public void rememberCurrentPlayerAndGiveControlToEveryone() {
        lastPlayer = getTurn();
        turn = - 1;
    }

    private short getLastPlayer() {
        return lastPlayer;
    }

    public short getTurn() {
        return turn;
    }

    //TODO: can same player put stop for himself?
    public boolean isTurnOpenToOtherPlayers() {
        return (turn == -1);
    }

    //TODO: skip move = change turn?
    public void toggleTurn() {
        switch (turn) {
            case -1: turn = getLastPlayer(); break;
            case 0: turn = 1; break;
            case 1: turn = 2; break;
            case 2: turn = 3; break;
            case 3: turn = 1; break;
            default: break;
        }

        if (turnChangeListener != null) {
            turnChangeListener.onTurnChanged(turn);
        }

    }
}
