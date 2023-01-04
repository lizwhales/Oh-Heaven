package oh_heaven.game;

import ch.aplu.jcardgame.*;

public class SmartNPC{
    private int playerNum;
    private static final int thinkingTime = 3000;

    public SmartNPC(int playerNum) {
        this.playerNum = playerNum;
    }

    public static Card randomCard(Hand hand) {
		int x = Oh_Heaven.random.nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}

    public Card play(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump, int bid, int tricks) {
        Card selected = null;
        Oh_Heaven.getInstance().setStatus("Player " + playerNum + " thinking...");
        Oh_Heaven.delay(thinkingTime);
        
        if (bid != tricks) {
            selected = winStrategy(hand, lead, trump);
            return selected;
        }
        // If bid == Tricks, STOP winning. Don't want to miss out on +10 point bonus
        selected = loseStrategy(hand, lead, trump);
        return selected;
        // if leading:
        
        // selected = randomCard(hand);
        // return selected;
    }

    public Card winStrategy(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump) {
        Card selected = null;
        if(lead == null){
            // first move function: find if you have ace of trump or if no trump ace then do a ace of any other suit
            selected = leadingFirstMove(hand, trump);
            return selected;
        } else {
            selected = notLeadingFirstMove(hand, lead, trump);
            return selected;
        }
    }

    public Card loseStrategy(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump) {
        Card selected = null;
        if(lead == null){
            return leadingLoseStrat(hand, lead, trump);
        } else {
            selected = notLeadingLoseStrat(hand, lead, trump);
            return selected;
        }
    }

    // if leading player within lose strat play highest ranked card from suit of least cards
    public Card leadingLoseStrat(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump){
        return playLowestOfLowestSuit(hand);
    }


    public Card playLowestOfLowestSuit(Hand hand) {
        // Play lowest ranked card from suit with least cards
        Card selected = null;
        int minCards = Oh_Heaven.nbStartCards;
        int lowestOfMinSuitCards = 100;
        for (Oh_Heaven.Suit suit: Oh_Heaven.Suit.values()) {
            int cardsOfSuit = 0;
            int lowestOfSuit = 100;
            int bestIndex = 0;
            for (int i = 0; i < hand.getNumberOfCards(); i++) {
                if ((Oh_Heaven.Suit) hand.get(i).getSuit() == suit) {
                    cardsOfSuit += 1;
                    if (hand.get(i).getRankId() < lowestOfSuit) {
                        lowestOfSuit = hand.get(i).getRankId();
                        bestIndex = i;
                    }
                }
            }
            if (cardsOfSuit < minCards) {
                lowestOfMinSuitCards = lowestOfSuit;
                selected = hand.get(bestIndex);
            } else if (cardsOfSuit == minCards) {
                if (lowestOfSuit < lowestOfMinSuitCards) {
                    lowestOfMinSuitCards = lowestOfSuit;
                    selected = hand.get(bestIndex);
                }
            }
        }
        
        return selected;
    }

    public Card notLeadingLoseStrat(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump){
        
        // play lowest cards
        Card selected = null;
        
        if (hand.getNumberOfCardsWithSuit(lead) > 0) {
            // If possessing card of lead suit, MUST play card of lead suit
            int worstRank = 1000;
            for(int i = 0; i < hand.getNumberOfCards(); i++) {
                if ((Oh_Heaven.Suit) hand.get(i).getSuit() == lead) {
                    if (hand.get(i).getRankId() < worstRank) {
                        selected = hand.get(i);
                        worstRank = hand.get(i).getRankId();
                    }
                }
            }
            return selected;
        } else if (hand.getNumberOfCards() == hand.getNumberOfCardsWithSuit(trump)) {
            // If hand has only trumps, return lowest trump
            int worstRank = 1000;
            for(int i = 0; i < hand.getNumberOfCards(); i++) {
                if (hand.get(i).getRankId() < worstRank) {
                    selected = hand.get(i);
                    worstRank = hand.get(i).getRankId();
                }
            }
            return selected;
        } else {
            // play highest non-trump
            int bestRank = 0;
            for(int i = 0; i < hand.getNumberOfCards(); i++) {
                if((Oh_Heaven.Suit) hand.get(i).getSuit() != trump){
                    // look for card within these suits REMBER TIO CHECK LEGALITY FIRST
                    if (hand.get(i).getRankId() > bestRank) {
                        selected = hand.get(i);
                        bestRank = hand.get(i).getRankId();
                    }
                }
            }
            return selected;
        }
    }

    public Card notLeadingFirstMove(Hand hand, Oh_Heaven.Suit lead, Oh_Heaven.Suit trump) {
        Card selected = null;
        if (hand.getNumberOfCardsWithSuit(lead) > 0) {
            for (int i = 0; i < hand.getNumberOfCards(); i++) {
                // if you have leading suit return highest of leading suit    
                if ((Oh_Heaven.Suit) hand.get(i).getSuit() == lead) {
                    if ((Oh_Heaven.Rank) hand.get(i).getRank() == TableObserver.getInstance().unplayedCards.get((Oh_Heaven.Suit) hand.get(i).getSuit())[0]){
                        selected = hand.get(i);
                        return selected;
                    }
                }    
            }
            // else, find best in suit
            int bestLeadRank = 0;
                for(int i = 0; i < hand.getNumberOfCards(); i++) {
                    if ((Oh_Heaven.Suit) hand.get(i).getSuit() == lead) {
                        if (hand.get(i).getRankId() > bestLeadRank) {
                            selected = hand.get(i);
                            bestLeadRank = hand.get(i).getRankId();
                        }
                    }  
                }
            return selected;
        } else {
            // Play best trump, if any
            if (hand.getNumberOfCardsWithSuit(trump) > 0) {
                int bestTrumpRank = 0;
                for(int i = 0; i < hand.getNumberOfCards(); i++) {
                    if ((Oh_Heaven.Suit) hand.get(i).getSuit() == trump) {
                        if (hand.get(i).getRankId() > bestTrumpRank) {
                            selected = hand.get(i);
                            bestTrumpRank = hand.get(i).getRankId();
                        }
                    }  
                }
                return selected;
            } else {
                // Play lowest of anything else
                int worstRank = 100;
                for(int i = 0; i < hand.getNumberOfCards(); i++) {
                    if (hand.get(i).getRankId() < worstRank) {
                        selected = hand.get(i);
                        worstRank = hand.get(i).getRankId();
                    }
                }
                return selected;
            }
        }
    }

    public Card leadingFirstMove(Hand hand, Oh_Heaven.Suit trump) {
        Card selected = null;
        
        for (int i = 0; i < hand.getNumberOfCards(); i++){
            if ((Oh_Heaven.Suit) hand.get(i).getSuit() == trump) {
                if ((Oh_Heaven.Rank) hand.get(i).getRank() == TableObserver.getInstance().unplayedCards.get(trump)[0]) {
                    System.out.println("Playing BEST TRUMP");
                    selected = hand.get(i);
                    // play ace trump
                    return selected;
                }
            }
        }

        for (int i = 0; i < hand.getNumberOfCards(); i++){
            if ((Oh_Heaven.Suit) hand.get(i).getSuit() != trump) {
                if ((Oh_Heaven.Rank) hand.get(i).getRank() == TableObserver.getInstance().unplayedCards.get((Oh_Heaven.Suit) hand.get(i).getSuit())[0]) {
                    System.out.println("Playing BEST NON-TRUMP");
                    selected = hand.get(i);
                    // play ace non-trump
                    return selected;
                }
            }
        }
       
        return playFromSuitWithLeastCards(hand);
    }


    public Card playFromSuitWithLeastCards(Hand hand) {
        // Play highest ranked card from suit with least cards
        Card selected = null;
        int minCards = Oh_Heaven.nbStartCards;
        int highestOfMinSuitCards = 0;
        for (Oh_Heaven.Suit suit: Oh_Heaven.Suit.values()) {
            int cardsOfSuit = 0;
            int highestOfSuit = 0;
            int bestIndex = 0;
            for (int i = 0; i < hand.getNumberOfCards(); i++) {
                if ((Oh_Heaven.Suit) hand.get(i).getSuit() == suit) {
                    cardsOfSuit += 1;
                    if (hand.get(i).getRankId() > highestOfSuit) {
                        highestOfSuit = hand.get(i).getRankId();
                        bestIndex = i;
                    }
                }
            }
            if (cardsOfSuit < minCards) {
                highestOfMinSuitCards = highestOfSuit;
                selected = hand.get(bestIndex);
            } else if (cardsOfSuit == minCards) {
                if (highestOfSuit > highestOfMinSuitCards) {
                    highestOfMinSuitCards = highestOfSuit;
                    selected = hand.get(bestIndex);
                }
            }
        }
        
        return selected;
    }

    public Boolean legalBool(Hand hand, Oh_Heaven.Suit lead, Card card) {
        if (lead != null) {
            while (card.getSuit() != lead && hand.getNumberOfCardsWithSuit(lead) > 0) {
                return false;
            }
        }
        return true;
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




// check if lead
// as following leader:
//  check have lead
//      check have highest lead card
//      play lowest lead card
//  
//  check dont have lead
//      play highest trump
//      if no trump, play lowest non lead/trump card

// lose strat

// lead:
//      card of suit with least cards
//      prioritise low (resolve for draw)
// not lead:
//      play highest non winning card
//      
