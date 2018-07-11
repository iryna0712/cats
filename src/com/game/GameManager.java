package com.game;

import com.GameSecurityManager;
import com.entities.Card;
import com.entities.CardType;
import com.entities.Deck;
import com.entities.Player;
import com.events.EventJSON;
import com.events.custom.AmountChangedJSON;
import com.events.custom.ConnectEventJSON;
import com.events.custom.GameOverEvent;
import com.events.custom.NextTurnEventJSON;
import com.events.custom.ShowCardsEventJSON;
import com.events.custom.StartEventJSON;
import com.interfaces.DeckFiller;
import com.mock.MockDeckFiller;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class GameManager implements ConnectionManager.IConnectionListener, Client.ClientStreamListener, TurnChangeListener{

    private static final Logger logger = Logger.getLogger(GameManager.class.getSimpleName());

    private static final int MAX_PLAYERS = 3;
    private static final int INITIAL_NUMBER_OF_PLAIN_CARDS = 3;

    volatile private boolean isGameInProcess;

    private AtomicInteger numPlayers = new AtomicInteger();

    private Map<Integer, Client> clients = new HashMap<>();
    private List<Player> players = new ArrayList<>(MAX_PLAYERS);
    private Deck deck;

    //TODO probably does not matter how many cards in stack, we display only top
    private Card stack;

    private ExternalEventPsychic psychic;

    //TODO: make private
    //TODO: nullable or not
    private Player getPlayer(int playerId) {
        Player playerToReturn = players.get(playerId - 1);
        return playerToReturn;
    }

    @Nullable
    private Player getPlayer(Client client) {
        Player playerToReturn = null;
        for (Player player : players) {
            if (player.getClientId() == client.getUniqueId()) {
                playerToReturn = player;
                break;
            }
        }
        return playerToReturn;
    }

    public GameManager() {
        stateManager = new StateManager();
        stateManager.setTurnChangeListener(this);
        eventDispatcher = new EventDispatcher(this, stateManager);
        psychic = new ExternalEventPsychic();
    }

    @Override
    public void onClientConnected(Client client) {
        //although connection manager should take care of maximum number of players, double check
        if (isGameInProcess) return;
        System.out.println("client connected " + client);

        //TODO: do not be stream listener
        client.setStreamListener(this);

        int clientId = client.getUniqueId();
        Player newPlayer = new Player(clientId);

        synchronized (this) {
            clients.put(clientId, client);
            players.add(newPlayer);
            //newPlayer.setExternalId((short)(players.indexOf(newPlayer) + 1));
        }

        numPlayers.incrementAndGet();
        System.out.println(numPlayers.intValue());
        if (numPlayers.intValue() == MAX_PLAYERS) {
            onAllPlayersConnected();
        }
    }

    //TODO: razobratsa s id vsemi. clientID - skrytyi, playerId = public. name=public, but not unique

    @Override
    //TODO: implement onClientDisconnected
    public void onClientDisconnected(Client client) {}

    //start only when all clients are connected
    public void onAllPlayersConnected() {
        isGameInProcess = true;

        psychic.setPlayers(players);
        psychic.setClients(clients);

        //create, fill deck and shuffle it
        DeckFiller filler = new MockDeckFiller();
        //DeckFiller filler = new DeckFillerImpl();
        deck = new Deck(filler);

        logger.severe("" + deck);

        //for each player give 1 protection card and 3 usual cards
        for (Player player : players) {
            giveCardToPlayer(player, new Card(CardType.SHIELD));

            for (short i = 0; i < INITIAL_NUMBER_OF_PLAIN_CARDS; i++) {
                checkCardFromDeckAndGiveToPlayer(player);
            }
        }

        for (Player player : players) logger.severe("" + player);

        new Thread(() -> waitWhilePlayersHandshake()).start();
    }

    public void waitWhilePlayersHandshake() {
        boolean allPlayersHandshaked = false;
        while (!allPlayersHandshaked) {
            logger.info("Waiting for players...");

            boolean allNamesAreDefined = true;
            for (Player player : players) {
                allNamesAreDefined &= ( player.getName() != null );
            }
            allPlayersHandshaked = allNamesAreDefined;
        }

        onAllPlayersHandshaked();
    }

    public void onAllPlayersHandshaked() {
        logger.severe("Players handshaked");
        //start event should be sent only when all players assigned their names
        for (Player player : players) {
            sendStartEvent(player);
        }

        //TODO: тесты с receiveMessage
        stateManager.switchTo(StateManager.GameState.WAITING_PLAYER);

        Client client1 = clients.get(players.get(0).getClientId());
        Client client2 = clients.get(players.get(1).getClientId());
        Client client3 = clients.get(players.get(2).getClientId());

        client1.receiveMessage("{\"event\":\"play\",\"card\":\"please\", \"id\":\"03\"}");
        //not his turn
        client2.receiveMessage("{\"event\":\"play\",\"card\":\"shuffle\"}");
        //not allowed
        client3.receiveMessage("{\"event\":\"play\",\"card\":\"bomb\"}");
        //does not have
        client3.receiveMessage("{\"event\":\"play\",\"card\":\"shuffle\"}");
        //should be ok
        client3.receiveMessage("{\"event\":\"play\",\"card\":\"cucumber\"}");
        client3.receiveMessage("{\"event\":\"play\",\"card\":\"cucumber\"}");
        client3.receiveMessage("{\"event\":\"play\",\"card\":\"cucumber\"}");
        client3.receiveMessage("{\"event\":\"play\",\"card\":\"cucumber\"}");

        client1.receiveMessage("{\"event\":\"takeFromDeck\"}");
        client1.receiveMessage("{\"event\":\"placeBomb\", \"isClosed\":true, \"place\":\"top\"}");

        //client2.receiveMessage("{\"event\":\"play\",\"card\":\"predict\"}");
        client2.receiveMessage("{\"event\":\"play\",\"card\":\"skip\"}");
        //client2.receiveMessage("{\"event\":\"takeFromDeck\"}");
        //client2.receiveMessage("{\"event\":\"placeBomb\", \"isClosed\":true, \"place\":\"top\"}");

        client3.receiveMessage("{\"event\":\"play\",\"card\":\"skip\"}");
        //client3.receiveMessage("{\"event\":\"play\",\"card\":\"shuffle\"}");
        //client3.receiveMessage("{\"event\":\"takeFromDeck\"}");
        //client3.receiveMessage("{\"event\":\"placeBomb\", \"isClosed\":false, \"place\":\"index\",\"index\":0}");

        client1.receiveMessage("{\"event\":\"play\",\"card\":\"cucumber\", \"id\":\"02\"}");
        client2.receiveMessage("{\"event\":\"takeFromDeck\"}");


    }

    @Override
    public synchronized void receive(Client client, String message) {
        logger.info("RECEIVE from " + getPlayer(client) + "\nMessage: " + message);

        //check if such client exists
        if (clients.containsKey(client.getUniqueId())) {
            EventJSON parsedEvent = ExternalEventBuilder.parseEvent(message);

            switch (parsedEvent.getEvent()) {
                case HANDSHAKE:
                {
                    ConnectEventJSON connectEventJSON = (ConnectEventJSON) parsedEvent;
                    String playerName = connectEventJSON.getName();
                    Player player = getPlayer(client);
                    player.setName(playerName);
                    break;
                }
                case TAKE_FROM_DECK:
                case PLAY:
                case PLACE_BOMB:
                {
                    Player player = getPlayer(client);
                    if (player != null) {
                        eventDispatcher.dispatchEvent(parsedEvent, player);
                    } else {
                        //TODO: implement GameSecurityManager that will kick players or clients
                        logger.severe("Received message from client, that is not in list of clients." +
                                "\nClient: " + client);
                    }
                    break;
                }
                default:
                    logger.severe("Client " + client + " sent and invalid event. Message: " + message);
                    break;
            }
        }
    }

    public void sendStartEvent(Player player) {
        Client client = clients.get(player.getClientId());

        if (client != null) {
            List<Player> otherPlayers = new ArrayList<>();
            //TODO: хитрый перебор
            for (Player playerFromList : players) {
                if (playerFromList != player) {
                    otherPlayers.add(playerFromList);
                }
            }

            StartEventJSON event = new StartEventJSON(deck.getSize());
            event.setHand(player.getCards());
            event.setTable(otherPlayers);
            psychic.send(client, event);
        }
    }
    //TODO: fabric of events?

    @Override
    public void onTurnChanged(int turn) {
        for (Player player : players) {
            sendTurnEvent(player);
        }
    }

    public void sendTurnEvent(Player player) {
        Client client = clients.get(player.getClientId());

        if (client != null) {
            NextTurnEventJSON event = new NextTurnEventJSON((short)stateManager.getTurn());
            psychic.send(client, event);
        }
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

    //TODO: maybe rename to open card, because it might be confsed with "deck"
    public void putCardToStack(Card card) {
        stack = card;
    }

    //TODO: responsible for setting state?
    public void setPlayerInactive(int playerId) {
        assert (playerId > 0);

        Player player = getPlayer(playerId);
        if (player != null) {
            player.setActive(false);

            AmountChangedJSON amountChangedJSON = new AmountChangedJSON(player.getExternalId(), 0);
            broadCastEventToOtherPlayers(player, amountChangedJSON);
        }

        boolean isGameOver = (getNumberOfActivePlayers() > 1);

        //TOD: parse game_Ver event in EVentDispatcher
        if (isGameOver) {
            stateManager.switchTo(StateManager.GameState.GAME_OVER);
            Player winner = getActivePlayer();
            if (winner != null) {
                broadCastEventToAllPlayers(new GameOverEvent(winner.getExternalId()));
            }
        }
    }

    @Nullable
    public Player getActivePlayer() {
        Player activePlayer = null;

        for (Player player : players) {
            if (player.isActive()) {
                activePlayer = player;
                break;
            }
        }
        return activePlayer;
    }

    public int getNumberOfActivePlayers() {
        short numActivePlayers = 0;

        for (Player player : players) {
            if (player.isActive()) {
                ++numActivePlayers;
            }
        }

        return numActivePlayers;
    }

    private StateManager stateManager;
    private EventDispatcher eventDispatcher;

    //TODO: add Nullable, NotNull where possible

    public boolean isTopCardBomb() {
        return deck.popTopCard().getType().equals(CardType.BOMB);
    }

    //TODO: rename to include bomb special case
    @Nullable
    public Card giveCardFromDeckToPlayer(int playerId) {
        //TODO: add asserts where needed
        assert (deck != null);
        assert (playerId > 0);
        Card card = null;

        Player player = getPlayer(playerId);
        if (player != null) {
            card = deck.popTopCard();
            if (!card.getType().equals(CardType.BOMB)) {
                giveCardToPlayer(player, card);
            }
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

    public void broadCastEventToPlayer(@Nullable Player player, EventJSON event) {
        if (player != null) {
            psychic.send(clients.get(player.getClientId()), event);
        }
    }

    public void broadCastEventToPlayer(short id, EventJSON event) {
        Player player = getPlayer(id);
        broadCastEventToPlayer(player, event);
    }

    public void broadCastEventToOtherPlayers(Player player, EventJSON event) {
        for (Player playerInList : players) {
            if (playerInList != player) {
                psychic.send(clients.get(playerInList.getClientId()), event);
            }
        }
    }

    //TODO: do we need this method
    public void broadCastEventToAllPlayers(EventJSON event) {
        for (Player playerInList : players) {
           psychic.send(clients.get(playerInList.getClientId()), event);
        }
    }

    public void kickPlayer(Player player) {
        if (player != null) {
            Client client = clients.get(player.getClientId());
            GameSecurityManager.getInstance().kickClient(client);
        }
    }

    public boolean takeCardFromPlayer(int playerId, Card card, int amount) {
        Player player = getPlayer(playerId);
        boolean result = takeCardFromPlayer(player, card, amount);
        return result;
    }

    //TODO: throw exeption if player doesnt have card
    public boolean takeCardFromPlayer(@Nullable Player player, Card card, int amount) {
        boolean result = false;

        if (player != null) {
            int amountToRemove = amount; //CardType.isPairCard(card.getEvent()) ? 2 : 1;
            result = player.hasCard(card, amountToRemove);
            //TODO: remake this pretty ugly thing! :)
            if (result) {
                player.removeCard(card);
                if (amountToRemove == 2) {
                    player.removeCard(card);
                }
            }
        }

        return result;
    }

    public boolean isGameInProcess() {
        return isGameInProcess;
    }

    public void show3CardsToPlayer(int playerId) {
        List<Card> top3Cards = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            top3Cards.add(deck.popTopCard());
        }

        Player player = getPlayer(playerId);

        ShowCardsEventJSON eventJSON = new ShowCardsEventJSON();
        eventJSON.setCards(top3Cards);
        broadCastEventToPlayer(player,eventJSON);

        Collections.reverse(top3Cards);
        for (Card card: top3Cards) {
            deck.placeCardAt(card, Deck.Place.TOP);
        }
    }

    //cards that player has is managed on server-side for safety
    private void giveCardToPlayer(@Nullable Player player, Card card) {
        if (player != null) {
            player.receiveCard(card);
        }
    }

    //TODO: mayber game manager is listener

    public void shuffleCards() {
        deck.shuffle();
    }

    public void placeCardToDeck( Card card, Deck.Place place) {
        deck.placeCardAt(card, place);
    }


}
