package oh_heaven.game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.util.*;
import oh_heaven.game.Oh_Heaven;
import oh_heaven.game.PlayerAdapter;


// Player Factory class used to get player adapters
public class Player {
    public static int totalBids = 0;
    private static final int bidDenominator = 4;
    private int playerNum;
    private int score = 0; 
    private int tricks = 0;
    private int bid = 0;
    private Hand hand;
    private String playerType;
    private PlayerAdapter playerAdapter = null;

    // Instantiate player
    public Player(int playerNum, String playerType) {
        this.playerNum = playerNum;
        this.playerType = playerType;
        if(playerType.equals("human")) {
            this.playerAdapter = (PlayerAdapter) new HumanAdapter(playerNum, hand);
        } else if (playerType.equals("random")) {
            this.playerAdapter = (PlayerAdapter) new RandomAdapter(playerNum, hand);
        } else if (playerType.equals("legal")) {
            this.playerAdapter = (PlayerAdapter) new LegalAdapter(playerNum);
        } else if (playerType.equals("smart")) {
            this.playerAdapter = (PlayerAdapter) new SmartAdapter(playerNum);
        }
    }

    // Attribute initializations
    
    public void initScores() {  
        score = 0;
    }

    public void initTricks() {
        tricks = 0;
    }

    public int initBid() {
        bid = Oh_Heaven.nbStartCards / bidDenominator + Oh_Heaven.random.nextInt(2);
        return bid;
    }

    public void initHand(Deck deck) {
        hand = new Hand(deck);
    }

    public void initHandView(CardGame oh_heaven, HandLayout layout) {
        hand.setView(oh_heaven, layout);
        hand.setTargetArea(new TargetArea(Oh_Heaven.trickLocation));
        hand.draw();
    }

    // Attribute modifications
    
    public void sortHand() {
        // hand.sort(Hand.SortType.SUITPRIORITY, true);
        playerAdapter.sortHand(hand, Hand.SortType.SUITPRIORITY, true);
    }

    public void updateScore() {
        score += tricks;
        if (tricks == bid) score += Oh_Heaven.madeBidBonus;
    }   
    
    public void alterBid() {
        if (bid == 0) {
            bid = 1;
        } else {
            bid += Oh_Heaven.random.nextBoolean() ? -1 : 1;
        }
    }

    public void dealingIn(Card card, boolean doDraw) {
        hand.insert(card, doDraw);
    }
    
    public void incrementTricks() {
        tricks++;
    }

    /*Getters for Player class to Oh_heaven*/

    public int getScore(){
        return score;
    }

    public int getBid(){
        return bid;
    }

    public int getTricks(){
        return tricks;
    } 

    public Hand getHand(){
        return hand;
    }

    // Pass to player adapter
    public Card play(Oh_Heaven.Suit lead, Oh_Heaven.Suit trump) {
        return playerAdapter.play(hand, lead, trump, bid, tricks);
    }

    // public Player(int playerNum, String playerType) {
    //     this.playerNum = playerNum;
    //     this.playerType = playerType;
        
    // }
}
