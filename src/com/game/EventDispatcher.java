package com.game;

import com.entities.Card;
import com.entities.CardType;
import com.entities.Deck;
import com.entities.Player;
import com.events.EventJSON;
import com.events.custom.AmountChangedJSON;
import com.events.custom.GiveAnyCardJSON;
import com.events.custom.PlaceBombEventJSON;
import com.events.custom.PlayCardEventJSON;
import com.events.custom.ReceiveEventJSON;
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
    private EventJSON pendingEvent;
    private short pendingPlayer = Player.INVALID_ID;

    //TODO: remake; game manager should not be set here?
    public EventDispatcher(GameManager gameManager, StateManager stateManager) {
        this.gameManager = gameManager;
        this.stateManager = stateManager;
    }

    private boolean isWaitingPlayer(Player player) {
        return (player.getExternalId() == pendingPlayer);
    }

    //TODO: synchronized?
    public void dispatchEvent(final EventJSON event, Player player) {
        logger.info("Event received: " + event);

        if (stateManager.getTurn() != player.getExternalId()) {
            boolean isRejectEvent = true;
            if (stateManager.isTurnOpenToOtherPlayers()&& event.getType().equals(EventJSON.EventJSONType.PLAY)) {
                PlayCardEventJSON playEventJSON = (PlayCardEventJSON)event;
                //TODO: do we need to check whether it is a cooldown
                if (playEventJSON.getCard().getType().equals(CardType.STOP)) {
                    isRejectEvent = false;
                }
            }

            if (isWaitingPlayer(player) && event.getType().equals(EventJSON.EventJSONType.PLAY)) {
                isRejectEvent = false;
            }

            if (isRejectEvent) {
                return;
            }
        }

        //TODO: COOLDOWN also allowed for put cards?
        GameState state = stateManager.getCurrentState();
        //TODO: check whether right person is playing
        if (
                !state.equals(GameState.WAITING_PLAYER) &&
                !state.equals(GameState.WAITING_PLAYER_GIVE) &&
                !(state.equals(GameState.COOL_DOWN) && event.getType().equals(EventJSON.EventJSONType.PLAY)) &&
                !(state.equals(GameState.BOMB_RECEIVED) && event.getType().equals(EventJSON.EventJSONType.PLACE_BOMB))) {
            return;
        }

        //switch to event dispatch
        if (!state.equals(GameState.COOL_DOWN)) {
            stateManager.switchTo(GameState.EVENT_RECEIVED);
        }

        //TODO: check whether card received from other player is not bomb, just for safety
        //TODO: check whether the state is waiting for event
        //TODO: fabrika po events

        switch (event.getType()) {
            case TAKE_FROM_DECK:
            {
                Card card = gameManager.giveCardToPlayer(player.getExternalId());

                if (card.getType().equals(CardType.BOMB)) {
                    Card shieldCard = Card.fromType(CardType.SHIELD);
                    if (player.hasCard(CardType.SHIELD)) {
                        gameManager.takeCardFromPlayer(player, shieldCard);
                        gameManager.putCardToStack(shieldCard);

                        //switch to bomb_received state and wait for bomb placement
                        stateManager.switchTo(GameState.BOMB_RECEIVED);
                    } else {
                        //TODO: do we need to keep count in stack? probably not
                        gameManager.putCardToStack(card);
                        gameManager.setPlayerInactive(player.getExternalId());
                        nextPlayer();
                    }
                } else {
                    EventJSON receiveEventJSON = new ReceiveEventJSON(card.getType());
                    gameManager.broadCastEventToPlayer(player, receiveEventJSON);

                    EventJSON amountChangedJSON = new AmountChangedJSON(player.getExternalId(), player.getCards().size());
                    gameManager.broadCastEventToOtherPlayers(player, amountChangedJSON);
                    //if player finished turn, switch to other player
                    nextPlayer();
                }
                break;
            }
            case PLAY:
            {
                PlayCardEventJSON playCardEventJSON = (PlayCardEventJSON) event;
                CardType cardType = playCardEventJSON.getCard().getType();
                Card card = new Card(cardType);
                if (isValidPlayCard(card)) {
                    //TODO: check if the state is waiting player
                    return;
                }

                if (stateManager.getCurrentState().equals(GameState.WAITING_PLAYER_GIVE)) {
                    pendingPlayer = Player.INVALID_ID;

                    short playerToGiveCardTo = stateManager.getTurn();
                    ReceiveEventJSON receiveEventJSON = new ReceiveEventJSON(card);
                    gameManager.broadCastEventToPlayer(playerToGiveCardTo, receiveEventJSON);

                    stateManager.switchTo(GameState.WAITING_PLAYER);
                }

                //TODO: check whether it is a shield

                boolean isCooldown1 = stateManager.getCurrentState().equals(GameState.COOL_DOWN);
                if (!isCooldown1) {
                    boolean result = gameManager.takeCardFromPlayer(player.getExternalId(), card);
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
                    stateManager.rememberCurrentPlayerAndGiveControlToEveryone();

                    pendingEvent = playCardEventJSON;
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

                //TODO: turn officially = when player has to put cards and then take (accept when skip)
                //not all other cases
                //TODO:find more proper place
                pendingEvent = null;

                if (card.getType().equals(CardType.RECEIVE_PLAYERS_CARD)) {
                    stateManager.switchTo(GameState.WAITING_PLAYER_GIVE);

                    pendingPlayer = playCardEventJSON.getId();
                    GiveAnyCardJSON giveAnyCardJSON = new GiveAnyCardJSON(player.getExternalId());
                    //stateManager.toggleTurn();
                    gameManager.broadCastEventToPlayer(pendingPlayer, giveAnyCardJSON);
                    return;
                }
                if (CardType.isPairCard(card.getType())) {
                    gameManager.putCardToStack(card);
                    stateManager.switchTo(GameState.WAITING_PLAYER_PICK);
                    return;
                }
                if (card.getType().equals(CardType.PREDICTION)) {
                    gameManager.show3CardsToPlayer(player.getExternalId());
                    //TODO: are we already in this state?
                    stateManager.switchTo(GameState.WAITING_PLAYER);
                    return;
                }
                if (card.getType().equals(CardType.SHUFFLE)) {
                    gameManager.shuffleCards();
                    //TODO: are we already in this state?
                    stateManager.switchTo(GameState.WAITING_PLAYER);
                    return;
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
                if (card.getType().equals(CardType.STOP)) {
                    //TODO: player cannot place stop in his turn
                    //gameManager.toggleStackCardActivity();
                    PlayCardEventJSON pendingPlayCardEvent = (PlayCardEventJSON)pendingEvent;
                    pendingPlayCardEvent.getCard().toggleActivity();
                    //TODO: are we already in this state?
                    stateManager.switchTo(GameState.WAITING_PLAYER);
                    return;
                }
                break;
            }
            case PLACE_BOMB:
            {
                PlaceBombEventJSON placeBombEventJSON = (PlaceBombEventJSON)event;
                boolean isClosed = placeBombEventJSON.isClosed();
                Deck.Place place = placeBombEventJSON.getPlace();

                if (place.equals(Deck.Place.INDEX)) {
                    place.setIndex(placeBombEventJSON.getIndex());
                }

                gameManager.placeCardToDeck(Card.fromType(CardType.BOMB), place);
                if (!isClosed) {
                    gameManager.broadCastEventToOtherPlayers(player, event);
                }
                nextPlayer();
                break;
            }
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

    private boolean isValidPlayCard(Card card) {
        return !card.isActive() || card.getType().equals(CardType.BOMB);
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
