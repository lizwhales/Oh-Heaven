package oh_heaven.game;

import ch.aplu.jcardgame.*;

public class RandomNPC{
    private int playerNum;
    private static final int thinkingTime = 2000;

    public RandomNPC(int playerNum, Hand hand) {
        this.playerNum = playerNum;
    }

    public static Card randomCard(Hand hand) {
		int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}

    public Card play(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump) {
        Card selected;
        Oh_Heaven.getInstance().setStatus("Player " + playerNum + " thinking...");
        Oh_Heaven.delay(thinkingTime);
        selected = randomCard(hand);
        return selected;
    }
}