package oh_heaven.game;

import ch.aplu.jcardgame.*;
import java.util.*;
import java.util.Arrays;

public class TableObserver implements TableListener {
    public HashMap<Oh_Heaven.Suit, Oh_Heaven.Rank[]> unplayedCards = new HashMap<Oh_Heaven.Suit, Oh_Heaven.Rank[]>();
    public HashMap<Integer, Oh_Heaven.Suit[]> unexhaustedSuits = new HashMap<Integer, Oh_Heaven.Suit[]>();
    private static TableObserver observerInstance = new TableObserver();

    public TableObserver() {
        for (Oh_Heaven.Suit suit: Oh_Heaven.Suit.values()) {
            unplayedCards.put(suit, Oh_Heaven.Rank.values());
        }
        // System.out.println(Oh_Heaven.Rank.values());

        for (int i = 0; i < Oh_Heaven.nbPlayers; i++) {
            unexhaustedSuits.put(i, Oh_Heaven.Suit.values());
        }
        // System.out.println(Oh_Heaven.Suit.values());
    }

    public void onCardPlay(int playerNum, Oh_Heaven.Suit lead, Card selected) {
        // System.out.println(selected.getRank().getClass()); 
        unplayedCards.put((Oh_Heaven.Suit) selected.getSuit(), removeElement_Rank(unplayedCards.get((Oh_Heaven.Suit) selected.getSuit()), (Oh_Heaven.Rank) selected.getRank()));
        if ((Oh_Heaven.Suit) selected.getSuit() != lead) {
            unexhaustedSuits.put(playerNum, removeElement_Suit(unexhaustedSuits.get(playerNum), (Oh_Heaven.Suit) selected.getSuit()));
        }
    }
    
    // Remove element from array, sourced from https://www.techiedelight.com/remove-element-from-array-java/
    public static Oh_Heaven.Rank[] removeElement_Rank(Oh_Heaven.Rank[] arr, Oh_Heaven.Rank rank) {
        return Arrays.stream(arr)
            .filter(s -> !s.equals(rank))
            .toArray(Oh_Heaven.Rank[]::new);
    }

    // Remove element from array, sourced from https://www.techiedelight.com/remove-element-from-array-java/
    public static Oh_Heaven.Suit[] removeElement_Suit(Oh_Heaven.Suit[] arr, Oh_Heaven.Suit suit) {
        return Arrays.stream(arr)
            .filter(s -> !s.equals(suit))
            .toArray(Oh_Heaven.Suit[]::new);
    }

    public static TableObserver getInstance() {
        return observerInstance;
    }
}
