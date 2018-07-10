package com.game;

import com.entities.Card;
import com.entities.CardType;
import com.entities.Player;
import com.events.AmountChangedJSON;
import com.events.EventJSON;
import com.events.PlayCardEventJSON;
import com.events.ReceiveEventJSON;
import com.game.StateManager.GameState;

import java.util.logging.Logger;

public class EventDispatcher {

    //TODO: mayber we don't manage states here, just put the card on stack and stack is polled

    //TODO: maybe this class will boradcst events?

    private static final Logger logger = Logger.getLogger(EventDispatcher.class.getSimpleName());

    private GameManager gameManager;
    private StateManager stateManager;

    //maybe should be in state manager
    private boolean isCoolDown;

    //TODO: remake; game manager should not be set here?
    public EventDispatcher(GameManager gameManager, StateManager stateManager) {
        this.gameManager = gameManager;
        this.stateManager = stateManager;
    }

    public void dispatchEvent(final EventJSON event, Player player) {
        logger.info("Event received: " + event);

        if (player.getNum() != stateManager.getTurn()) {
            return;
        }

        //TODO: COOLDOWN also allowed for put cards?
        GameState state = stateManager.getCurrentState();
        //TODO: check whether right person is playing
        if (
                !state.equals(GameState.WAITING_PLAYER) &&
                !(state.equals(GameState.COOL_DOWN) && event.getEvent().equals(EventJSON.EventJSONType.PLAY))) {
            return;
        }

        //switch to event dispatch
        if (!state.equals(GameState.COOL_DOWN)) {
            stateManager.switchTo(GameState.EVENT_RECEIVED);
        }


        //TODO: check whether card received from other player is not bomb, just for safety
        //TODO: check whether the state is waiting for event
        //TODO: fabrika po events

        switch (event.getEvent()) {
            case TAKE_FROM_DECK:
            {
                Card card = gameManager.giveCardToPlayer(player.getNum());
                EventJSON receiveEventJSON = new ReceiveEventJSON(card.getType());
                gameManager.broadCastEventToPlayer(player, receiveEventJSON);

                if (card.getType().equals(CardType.BOMB)) {
                    stateManager.switchTo(GameState.BOMB_RECEIVED);
                } else {
                    EventJSON amountChangedJSON = new AmountChangedJSON((short)player.getNum(), player.getCards().size());
                    gameManager.broadCastEventToOtherPlayers(player, amountChangedJSON);
                    //TODO: broadcast event num card changed
                    //if player finished turn, switch to other player
                    nextPlayer();
                }
                break;
            }
            case PLAY: {
                PlayCardEventJSON playCardEventJSON = (PlayCardEventJSON) event;
                CardType cardType = playCardEventJSON.getCard();
                Card card = new Card(cardType);

                boolean isCooldown1 = stateManager.getCurrentState().equals(GameState.COOL_DOWN);
                if (!isCooldown1) {
                    boolean result = gameManager.takeCardFromPlayer(player.getNum(), card);
                    if (!result) {
                        logger.severe(
                                "Player " + player + " is trying to play card he doesn't have on hand!" +
                                        "\n CardType: " + cardType);
                        //TODO: maybe kick player
                        return;
                    }

                    //TODO: just for testing, maybe we will not need it
                    gameManager.putCardToStack(card);
                    gameManager.broadCastEventToOtherPlayers(player, event);
                }

                //if it is not cooldown and we received a stoppable card, let other players interrupt
                boolean isCooldown = stateManager.getCurrentState().equals(GameState.COOL_DOWN);
                if (!isCooldown && CardType.isCardStoppable(card.getType())) {
                    stateManager.switchTo(GameState.COOL_DOWN);

                    dispatchEvent(event, player);

                    //TODO: uncomment later
//                    TimerTask timerTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            logger.info("Cooldown for card " + card + " stopped");
//                            NoStopCardEventJSON noStopCardEventJSON = new NoStopCardEventJSON();
//                            gameManager.broadCastEventToOtherPlayers(player, noStopCardEventJSON);
//
//                            //rethrow event
//                            dispatchEvent(event, player);
//                        }
//                    };
//                    Timer timer = new Timer("Cooldown_timer");
//                    timer.schedule(timerTask, 1000);
                    //gameManager.startCoolDown();
                    return;
                }

                if (isCooldown) {
                    //TODO: do we need to switch (switch later actually)
                    //stateManager.switchTo(GameState.WAITING_PLAYER);
                }

                if (stateManager.getCurrentState().equals(GameState.BOMB_RECEIVED)) {
//                    //TODO: check whether player has shield, do it automatically
//                    if (card.getType().equals(CardType.SHIELD)) {
//                        gameManager.putCardToStack(card);
//                        Deck.Place place = event.getPlace();
//                        //TODO:
//                        gameManager.placeCardToDeck(new Card(CardType.BOMB), place);
//                        //TODO: put bomb?
//                        nextPlayer();
//                    } else {
//                        //TODO: send error
//                    }
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
                        gameManager.show3CardsToPlayer(player.getNum());
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
                    //TODO: automatically change to WAITING_PLAYER upon certain cards
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
            case PLACE_BOMB:
//            case GIVE_CARD:
//            {
//                gameManager.giveCardToPlayer(stateManager.getTurn(), event.getCard());
//                //player can make another move until TAKE_CARD_FROM_DECK
//                stateManager.switchTo(GameState.WAITING_PLAYER);
//                break;
//            }
//            case GIVE_UP:
//            {
//                gameManager.setPlayerInactive(event.getNumPlayer());
//                break;
//            }

            default:
                logger.info(
                        "Unknown event received: " + event +
                        " from Player: " + player);
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
