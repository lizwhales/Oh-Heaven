package oh_heaven.game;

import ch.aplu.jcardgame.*;
import static ch.aplu.jgamegrid.GameGrid.delay;

public class Human {
    private int playerNum;

    private Card selected;

    public Human(int playerNum, Hand hand){
        this.playerNum = playerNum;
    }
    
    public Card play(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump) {
        selected = null;
        hand.setTouchEnabled(true);
        while(null == selected) delay(100);
        return selected;
    }

    public int initBid() {
        int bid = Oh_Heaven.nbStartCards / 4 + Oh_Heaven.random.nextInt(2);
        return bid;
    }

    public void addListener(Hand hand) {
        CardListener cardListener = new CardAdapter() {
            public void leftDoubleClicked(Card card){
                selected = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
    }
}
