package oh_heaven.game;

import ch.aplu.jcardgame.*;

public class LegalAdapter implements PlayerAdapter{
    private LegalNPC legalNPC;

    public LegalAdapter(int playerNum) {
        legalNPC = new LegalNPC(playerNum);
    }
    
    @Override
    public Card play(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump, int bid, int tricks){
        return legalNPC.play(hand, lead, trump);
    }

    public int initBid() {
        int bid = Oh_Heaven.nbStartCards / 4 + Oh_Heaven.random.nextInt(2);
        return bid;
    }

    public void sortHand(Hand hand, Hand.SortType sortType, boolean doDraw) {
        hand.sort(Hand.SortType.SUITPRIORITY, true);
    }
}
