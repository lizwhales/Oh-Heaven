package oh_heaven.game;

import ch.aplu.jcardgame.*;
import oh_heaven.game.Oh_Heaven.Suit;

public class SmartAdapter implements PlayerAdapter {
    private SmartNPC smartNPC;

    public SmartAdapter(int playerNum) {
        smartNPC = new SmartNPC(playerNum);
    }

    public Card play(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump, int bid, int tricks) {
        return smartNPC.play(hand, lead, trump, bid, tricks);
    }

    public int initBid() {
        int bid = Oh_Heaven.nbStartCards / 4 + Oh_Heaven.random.nextInt(2);
        return bid;
    }
    
    public void sortHand(Hand hand, Hand.SortType sortType, boolean doDraw) {
        hand.sort(Hand.SortType.SUITPRIORITY, true);
    }

}
