package oh_heaven.game;

import ch.aplu.jcardgame.*;

public class LegalNPC {
    private int playerNum;
    private static final int thinkingTime = 2000;

    public LegalNPC(int playerNum) {
        this.playerNum = playerNum;
    }
    
    public static Card randomCard(Hand hand) {
		int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}

    public Card play(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump){
        Card selected;
        Oh_Heaven.getInstance().setStatus("Player " + playerNum + " thinking...");
        Oh_Heaven.delay(thinkingTime);
        selected = randomCard(hand);

        // behaviour to only return legal moves
        selected = checkLegality(hand, lead, selected);

        return selected;
    }

    public Card checkLegality(Hand hand, Oh_Heaven.Suit lead, Card card) {
        if (lead != null) {
            while (card.getSuit() != lead && hand.getNumberOfCardsWithSuit(lead) > 0) {
                // Card does not follow rules, re-randomize selection
                card = randomCard(hand);
            }
        }
        return card;
    }

    
}
