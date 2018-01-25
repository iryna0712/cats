package java.game;

import java.entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameManager {

    private static final Logger logger = Logger.getLogger(GameManager.class.getSimpleName());

    private static final int MAX_PLAYERS = 6;
    private static final int INITIAL_NUMBER_OF_PLAIN_CARDS = 3;

    private int numPlayers = 2;

    private List<Player> players = new ArrayList<>(MAX_PLAYERS);
    private Deck deck;


    public GameManager() {

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("regular_player"));
        }

        //create and fill deck
        DeckFiller filler = new DeckFiller();
        deck = new Deck(filler);

        //for each player give 1 protection card and 3 usual cards
        for (Player player : players) {
            player.receiveCard(new Card(CardType.SHIELD));

            for (short i = 0; i < INITIAL_NUMBER_OF_PLAIN_CARDS; i++) {
                checkCardFromDeckAndGiveToPlayer(player);
            }
        }

        for (Player player : players) logger.finest("" + player);

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
                player.receiveCard(topCard);
                cardReceived = true;
            }
        }
    }
}
