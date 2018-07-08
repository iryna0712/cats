package com.game;

import com.entities.Card;
import com.entities.CardType;
import com.entities.Deck;
import com.game.StateManager.GameState;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class EventDispatcher {

    //TODO: mayber we don't manage states here, just put the card on stack and stack is polled

    private static final Logger logger = Logger.getLogger(EventDispatcher.class.getSimpleName());


    private GameManager gameManager;
    private StateManager stateManager;

    //TODO: remake; game manager should not be set here?
    public EventDispatcher(GameManager gameManager, StateManager stateManager) {
        this.gameManager = gameManager;
        this.stateManager = stateManager;
    }

    public void dispatchEvent(final Event event) {
        logger.info("Event received: " + event);

        if (event.getNumPlayer() != stateManager.getTurn()) {
            return;
        }

        if (!stateManager.getCurrentState().equals(GameState.WAITING_PLAYER)) {
            return;
        }

        //switch to event dispatch
        stateManager.switchTo(GameState.EVENT_RECEIVED);

        //TODO: check whether te state is waiting for event

        switch (event.getType()) {
            case TAKE_CARD_FROM_DECK:
            {
                Card card = gameManager.giveCardToPlayer(event.getNumPlayer());
                if (card.getType().equals(CardType.BOMB)) {
                    stateManager.switchTo(GameState.BOMB_RECEIVED);
                } else {
                    //if player finished turn, switch to other player
                    nextPlayer();
                }
                break;
            }
            case PUT_CARD:
            {
                Card card = event.getCard();
                //TODO: if it is pair card, remove two;
                boolean result = gameManager.takeCardFromPlayer(event.getNumPlayer(), card);
                if (!result) {
                    //TODO: log error, trying to put card which does not exist in player's stack
                    return;
                }

                //if it is not cooldown and we received a stoppable card, let other players interrupt
                boolean isCooldown = stateManager.getCurrentState().equals(GameState.COOL_DOWN);
                if (!isCooldown && CardType.isCardStoppable(card.getType())) {
                    stateManager.switchTo(GameState.COOL_DOWN);

                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            logger.info("Cooldown for card " + card + " stopped");
                            //stateManager.switchTo(GameState.WAITING_PLAYER);
                            EventDispatcher.this.dispatchEvent(event);
                        }
                    };
                    Timer timer = new Timer("Cooldown_timer");
                    timer.schedule(timerTask, 1000);
                    //gameManager.startCoolDown();
                    return;
                }

                if (isCooldown) {
                    //TODO: do we need to switch
                    stateManager.switchTo(GameState.WAITING_PLAYER);
                }

                if (stateManager.getCurrentState().equals(GameState.BOMB_RECEIVED)) {
                    //TODO: check whether player has shield, do it automatically
                    if (card.getType().equals(CardType.SHIELD)) {
                        gameManager.putCardToStack(card);
                        Deck.Place place = event.getPlace();
                        //TODO:
                        gameManager.placeCardToDeck(new Card(CardType.BOMB), place);
                        //TODO: put bomb?
                        nextPlayer();
                    } else {
                        //TODO: send error
                    }
                } else {
                    if (card.getType().equals(CardType.RECEIVE_PLAYERS_CARD)) {
                        stateManager.switchTo(GameState.WAITING_PLAYER_GIVE);
                        //stateManager.toggleTurn();
                        return;
                    }
                    if (CardType.isPairCard(card.getType())) {
                        gameManager.putCardToStack(card);
                        stateManager.switchTo(GameState.WAITING_PLAYER_PICK);
                        return;
                    }
                    if (card.getType().equals(CardType.PREDICTION)) {
                        gameManager.show3CardsToPlayer(event.getNumPlayer());
                        //TODO: are we already in this state?
                        stateManager.switchTo(GameState.WAITING_PLAYER);
                        return;
                    }
                    if (card.getType().equals(CardType.SHUFFLE)) {
                        gameManager.shuffleCards();
                        //TODO: are we already in this state?
                        stateManager.switchTo(GameState.WAITING_PLAYER);
                    }
                    if (card.getType().equals(CardType.SKIP_MOVE)) {
                        nextPlayer();
                    }
                    if (card.getType().equals(CardType.ATTACK)) {
                        if (stateManager.getCurrentState().equals(GameState.WAITING_PLAYER_DOUBLE_MOVE)) {
                            nextPlayer(true);
                            return;
                        }

                        stateManager.switchTo(GameState.WAITING_PLAYER_DOUBLE_MOVE);
                        stateManager.toggleTurn();
                    }
                }
                break;
            }
            case GIVE_CARD:
            {
                gameManager.giveCardToPlayer(stateManager.getTurn(), event.getCard());
                //player can make another move until TAKE_CARD_FROM_DECK
                stateManager.switchTo(GameState.WAITING_PLAYER);
                break;
            }
            case GIVE_UP:
            {
                gameManager.setPlayerInactive(event.getNumPlayer());
                break;
            }

            default:
                //TODO: log
                break;

        }
    }

    private void nextPlayer() {
        nextPlayer(false);
    }

    private void nextPlayer(boolean isForced) {
        //if the player received "Attack" card previously, then it is his turn again
        //TODO: replace state with turn counter
        if (isForced || !stateManager.getCurrentState().equals(GameState.WAITING_PLAYER_DOUBLE_MOVE)) {
            stateManager.toggleTurn();
        }
        stateManager.switchTo(GameState.WAITING_PLAYER);
    }
}
