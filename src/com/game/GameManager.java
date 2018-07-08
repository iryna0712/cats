package com.game;

import com.entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interfaces.DeckFiller;
import com.messages.Message;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class GameManager implements ConnectionManager.IConnectionListener {

    private static final Logger logger = Logger.getLogger(GameManager.class.getSimpleName());

    private static final int MAX_PLAYERS = 2;
    private static final int INITIAL_NUMBER_OF_PLAIN_CARDS = 3;

    volatile private boolean isGameInProcess;

    private AtomicInteger numPlayers = new AtomicInteger();

    private Map<Integer, Client> clients = new HashMap<>();
    private List<Player> players = new ArrayList<>(MAX_PLAYERS);
    private Deck deck;

    //TODO probably does not matter how many cards in stack, we display only top
    private Card stack;

    private Client sampleClient;

    //TODO: make private
    public Player getPlayer(int playerId) {
        Player playerToReturn = null;

        playerToReturn = players.get(playerId - 1);
//        for (Player player : players) {
//            if (player.() == playerId) {
//                playerToReturn = player;
//            }
//
//        }
        return playerToReturn;
    }

    public GameManager() {
        stateManager = new StateManager();
        eventDispatcher = new EventDispatcher(this, stateManager);

//        for (int i = 0; i < MAX_PLAYERS; i++) {
//            Player newPlayer = new Player(i, "regular_player");
//            players.add(newPlayer);
//        }

//        try {
//            sampleClient = new SampleClient(new Socket());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //start();

        /*try {
            ConnectionManager connectionManager = new ConnectionManager(MAX_PLAYERS, this);
            connectionManager.openForConnectionsAndWait();
        } catch (IOException e) {
            //error during closing of server socket
        }*/
    }

    @Override
    public void onClientConnected(Client client) {
        //although connection manager should take care of maximum number of players, double check
        if (isGameInProcess) return;
        System.out.println("client connected " + client);

        int clientId = client.hashCode();
        Player newPlayer = new Player(clientId, "regular_player");

        synchronized (this) {
            clients.put(clientId, client);
            players.add(newPlayer);
            newPlayer.setNum(players.indexOf(newPlayer) + 1);
        }

        numPlayers.incrementAndGet();
        if (numPlayers.intValue() == MAX_PLAYERS) {
            start();
        }
    }

    @Override
    public void onClientDisconnected(Client client) {

    }

    //when all clients are connected
    public void start() {
        isGameInProcess = true;

        //TODO:: maybe deck filler should also shuffle?
        //create, fill deck and shuffle it
        DeckFiller filler = new MockDeckFiller();
        //DeckFiller filler = new DeckFillerImpl();
        deck = new Deck(filler);
        //deck.shuffle();

        logger.severe("" + deck);

        //for each player give 1 protection card and 3 usual cards
        for (Player player : players) {
            giveCardToPlayer(player, new Card(CardType.SHIELD));

            for (short i = 0; i < INITIAL_NUMBER_OF_PLAIN_CARDS; i++) {
                checkCardFromDeckAndGiveToPlayer(player);
            }
        }

        for (Player player : players) logger.severe("" + player);

        stateManager.switchTo(StateManager.GameState.WAITING_PLAYER);

//        while (!deck.isEmpty()) {
//            Card card = deck.popTopCard();
//            logger.severe("Card popped: " + card);
//        }

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    int a = 0;
//                }
//            }
//            });
//        thread.start();
        takeCardEvent(1);
        takeCardEvent(2);
        takeCardEvent(1);
        takeCardEvent(2);
        putCardEvent(1, CardType.ATTACK);
        putCardEvent(2, CardType.PREDICTION);
    }

    public void putCardEvent(int numPlayer, CardType type) {
        Event event = new Event(players.get(numPlayer - 1), Event.EventType.PUT_CARD);
        event.setCard(new Card(type));

        getEventDispatcher().dispatchEvent(event);
    }

    public void takeCardEvent(int numPlayer) {
        Event event = new Event(getPlayer(numPlayer), Event.EventType.TAKE_CARD_FROM_DECK);
        //event.setCard();
        getEventDispatcher().dispatchEvent(event);
    }

    private void checkCardFromDeckAndGiveToPlayer(Player player) {
        assert (deck != null);

        boolean cardReceived = false;

        while(!cardReceived) {
            Card topCard = deck.popTopCard();

            if (CardType.BOMB == topCard.getType() || CardType.SHIELD == topCard.getType()) {
                //return card if it is not legit to give in the beginning
                deck.addCardAndShuffle(topCard);
            } else {
                giveCardToPlayer(player, topCard);
                //player.receiveCard(topCard);
                cardReceived = true;
            }
        }
    }

    public void putCardToStack(Card card) {
        stack = card;
    }

    //TODO: responsible for setting state?
    public void setPlayerInactive(int playerId) {
        assert (playerId > 0);

        Player player = getPlayer(playerId);
        if (player != null) {
            player.setActive(false);
        }

        //TODO: check whether >1 active players
        //to state GAME_OVER
    }

    private StateManager stateManager;
    private EventDispatcher eventDispatcher;

//    public void startCooldown() {
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                stateManager.switchTo(StateManager.);
//
//            }
//        }
//
//        Timer timer = new Timer()
//
//    }

    @Nullable
    public Card giveCardToPlayer(int playerId) {
        assert (deck != null);
        assert (playerId > 0);

        Card card = null;

        Player player = getPlayer(playerId);
        if (player != null) {
            card = deck.popTopCard();
            giveCardToPlayer(player, card);
        }
        return card;
    }

    public void giveCardToPlayer(int playerId, Card card) {
        if (card == null) {
            return;
        }

        Player player = getPlayer(playerId);
        if (player != null) {
            giveCardToPlayer(player, card);
        }
    }

    public boolean takeCardFromPlayer(int playerId, Card card) {
        Player player = getPlayer(playerId);
        boolean result = false;
        if (player != null) {
            result = player.removeCard(card);
        }
        return result;
    }

    public void show3CardsToPlayer(int playerId) {
        List<Card> top3Cards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            top3Cards.add(deck.popTopCard());
        }

        //TODO: send 3 cards info to player

        //TODO: only on event received return cards to deck
        for (Card card: top3Cards) {
            deck.placeCardAt(card, Deck.Place.TOP);
        }
    }

    //TODO: remove getEventdispatcher and setEventDispatcher
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    //cards that player has is managed on server-side for safety
    private void giveCardToPlayer(Player player, Card card) {
        player.receiveCard(card);

        //move this to communicator
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Message message = new Message(Message.CARD, mapper.writeValueAsString(card));
//            System.out.print("to player : " + player.getId() + " ");
//            clients.getOrDefault(player.getId(), sampleClient).sendMessage(message);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }

    //TODO: mayber game manager is listener

    public void shuffleCards() {
        deck.shuffle();
    }

    public void placeCardToDeck( Card card, Deck.Place place) {
        deck.placeCardAt(card, place);
    }


}
