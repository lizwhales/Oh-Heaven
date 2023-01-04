package oh_heaven.game;

import ch.aplu.jcardgame.*;

public interface PlayerAdapter {
    // Player adapter interface to be used by player factory
    public Card play(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump, int bid, int tricks);
    public int initBid();
    public void sortHand(Hand hand, Hand.SortType sortType, boolean doDraw);
}