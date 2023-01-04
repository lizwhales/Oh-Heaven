package oh_heaven.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;
import oh_heaven.game.Player;
import oh_heaven.game.PropertiesLoader;


@SuppressWarnings("serial")
public class Oh_Heaven extends CardGame {
	private static Oh_Heaven single_instance = null;
	
	public enum Suit
	{
		SPADES, HEARTS, DIAMONDS, CLUBS
	}

	public enum Rank
	{
		// Reverse order of rank importance (see rankGreater() below)
		// Order of cards is tied to card images
		ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
	}
  
	final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

	static public int seed = 30006;
	static public Random random = new Random(seed);
  
	// return random Enum value
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	// return random Card from Hand
	public static Card randomCard(Hand hand) {
		int x = random.nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}
 
	// return random Card from ArrayList
	public static Card randomCard(ArrayList<Card> list) {
		int x = random.nextInt(list.size());
		return list.get(x);
	}
  
	private void dealingOut() {
		Hand pack = deck.toHand(false);
		// pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
		for (int i = 0; i < nbStartCards; i++) {
			for (int j=0; j < nbPlayers; j++) {
				if (pack.isEmpty()) return;
				Card dealt = randomCard(pack);
				// System.out.println("Cards = " + dealt);
				dealt.removeFromHand(false);
				/* bring it back to life within Player*/
				players[j].dealingIn(dealt, false);
				// hands[j].insert(dealt, false);
				// dealt.transfer(hands[j], true);
			}
		}
	}
  

    private void initScores() {
		for(int i = 0; i < nbPlayers; i++){
			players[i].initScores();	
		}
	}

	private void updateScores() {
		for (int i = 0; i < nbPlayers; i++) {
			players[i].updateScore();
		}
	}

	private void initTricks() {
		for (int i = 0; i < nbPlayers; i++) {
			players[i].initTricks();
		}
	}

	private void initBids(Suit trumps, int nextPlayer) {
		Player.totalBids = 0;
		for (int i = nextPlayer; i < nextPlayer + nbPlayers; i++) {
			int iP = i % nbPlayers;
			int playerbids = players[iP].initBid();
			Player.totalBids += playerbids;
		}
		if (Player.totalBids == nbStartCards) {  // Force last bid so not every bid possible
			int iP = (nextPlayer + nbPlayers) % nbPlayers;
			players[iP].alterBid();
		}
	}

  
	public boolean rankGreater(Card card1, Card card2) {
		return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
	}
		
	private final String version = "1.0";
	public static int nbPlayers = 4;
	public static int nbStartCards = 13;
	public static int nbRounds = 3;
	public static final int madeBidBonus = 10;
	private final int handWidth = 400;
	private final int trickWidth = 40;
	private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private final Location[] handLocations = {
				new Location(350, 625),
				new Location(75, 350),
				new Location(350, 75),
				new Location(625, 350)
		};
	private final Location[] scoreLocations = {
				new Location(575, 675),
				new Location(25, 575),
				new Location(575, 25),
				// new Location(650, 575)
				new Location(575, 575)
		};
	private Actor[] scoreActors = {null, null, null, null };
	public static final Location trickLocation = new Location(350, 350);
	public static final Location textLocation = new Location(350, 450);
	private final int thinkingTime = 2000;
	// private Hand[] hands;
	private Location hideLocation = new Location(-500, - 500);
	private Location trumpsActorLocation = new Location(50, 50);
	private boolean enforceRules=false;

	public void setStatus(String string) {setStatusText(string); }
	
	// private int[] scores = new int[nbPlayers];
	// private int[] tricks = new int[nbPlayers];
	// private int[] bids = new int[nbPlayers];
	private Player[] players = new Player[nbPlayers];

	Font bigFont = new Font("Serif", Font.BOLD, 36);

	private void initScore() {
		for (int i = 0; i < nbPlayers; i++) {
			// scores[i] = 0;
			String text = "[" + String.valueOf(players[i].getScore()) + "]" + String.valueOf(players[i].getTricks()) + "/" + String.valueOf(players[i].getBid());
			scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
			addActor(scoreActors[i], scoreLocations[i]);
		}
	}

	private void updateScore(int i) {
		removeActor(scoreActors[i]);
		String text = "[" + String.valueOf(players[i].getScore()) + "]" + String.valueOf(players[i].getTricks()) + "/" + String.valueOf(players[i].getBid());
		scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[i], scoreLocations[i]);
	}


	private Card selected;

	private void initRound() {
			// hands = new Hand[nbPlayers];
			for (int i = 0; i < nbPlayers; i++) {
				//hands[i] = new Hand(deck); SEE PLAYER 
				players[i].initHand(deck);
			} 
			dealingOut();
			for (int i = 0; i < nbPlayers; i++) {
				players[i].sortHand();
				// hands[i].sort(Hand.SortType.SUITPRIORITY, true);
			}
			// Set up human player for interaction
			// CardListener cardListener = new CardAdapter()  // Human Player plays card
			// 		{
			// 		public void leftDoubleClicked(Card card) { selected = card; hands[0].setTouchEnabled(false); }
			// 		};
			// hands[0].addCardListener(cardListener);
			// graphics
			RowLayout[] layouts = new RowLayout[nbPlayers];
			for (int i = 0; i < nbPlayers; i++) {
				layouts[i] = new RowLayout(handLocations[i], handWidth);
				layouts[i].setRotationAngle(90 * i);
				// layouts[i].setStepDelay(10);
				players[i].initHandView(this, layouts[i]);
				// CHANGED FROM PLAYER CLASS
				//hands[i].setView(this, layouts[i]);
				//hands[i].setTargetArea(new TargetArea(trickLocation));
				//hands[i].draw();
			}
	//	    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
	//	      hands[i].setVerso(true);			// You do not need to use or change this code.
			// End graphics
	}

	private void playRound() {
		// Select and display trump suit
		final Suit trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);

		// End trump suit
		Hand table;
		int winner;
		Card winningCard;
		Suit lead = null;
		int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
		initBids(trumps, nextPlayer);

		// Initialize table Observer
		TableListener tableListener = new TableObserver();
		

		// initScore();
		for (int i = 0; i < nbPlayers; i++) updateScore(i);
		
		// Turn begins here
		for (int i = 0; i < nbStartCards; i++) {
			// RESET THE LEAD at the start of each turn
			lead = null;
			table = new Hand(deck);
			selected = null;
			// if (false) {
			// if (0 == nextPlayer) {  // Select lead depending on player type
			// 	hands[0].setTouchEnabled(true);
			// 	setStatus("Player 0 double-click on card to lead.");
			// 	while (null == selected) delay(100);
			// } else {
			// 	setStatusText("Player " + nextPlayer + " thinking...");
			// 	delay(thinkingTime);
			// 	selected = randomCard(players[nextPlayer].getHand());
			// }
			// NEW, Lead can be null
			
			selected = players[nextPlayer].play(lead, trumps);
			// Forward played card to tableObserver
			tableListener.onCardPlay(nextPlayer, lead, selected);

			// Lead with selected card
			table.setView(this, new RowLayout(trickLocation, (table.getNumberOfCards()+2)*trickWidth));
			table.draw();
			selected.setVerso(false);
			// No restrictions on the card being lead
			lead = (Suit) selected.getSuit();
			selected.transfer(table, true); // transfer to trick (includes graphic effect)
			winner = nextPlayer;
			winningCard = selected;

			// End Lead
			for (int j = 1; j < nbPlayers; j++) {
				if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
				selected = null;
				// if (false) {	
				// if (0 == nextPlayer) {
				// 	hands[0].setTouchEnabled(true);
				// 	setStatus("Player 0 double-click on card to follow.");
				// 	while (null == selected) delay(100);
				// } else {
				// 	setStatusText("Player " + nextPlayer + " thinking...");
				// 	delay(thinkingTime);
				// 	selected = randomCard(players[nextPlayer].getHand());
				// }
				// NEW

				selected = players[nextPlayer].play(lead, trumps);
				tableListener.onCardPlay(nextPlayer, lead, selected);

				// Follow with selected card
				table.setView(this, new RowLayout(trickLocation, (table.getNumberOfCards()+2)*trickWidth));
				table.draw();
				selected.setVerso(false);  // In case it is upside down
				
				// Check: Following card must follow suit if possible
				if (selected.getSuit() != lead && players[nextPlayer].getHand().getNumberOfCardsWithSuit(lead) > 0) {
					// Rule violation
					String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
					System.out.println(violation);
					if (enforceRules) try {
						throw(new BrokeRuleException(violation));
					} catch (BrokeRuleException e) {
						e.printStackTrace();
						System.out.println("A cheating player spoiled the game!");
						System.exit(0);
					}  
				}
				// End Check
				selected.transfer(table, true); // transfer to trick (includes graphic effect)
				System.out.println("winning: " + winningCard);
				System.out.println(" played: " + selected);
				// System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
				// System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " + (13 -    selected.getRankId()));
				if ( // beat current winner with higher card
					(selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
					// trumped when non-trump was winning
					(selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
					System.out.println("NEW WINNER");
					winner = nextPlayer;
					winningCard = selected;
				}
				// End Follow
			}
			delay(600);
			table.setView(this, new RowLayout(hideLocation, 0));
			table.draw();		
			nextPlayer = winner;
			setStatusText("Player " + nextPlayer + " wins trick.");
			players[nextPlayer].incrementTricks();
			// tricks[nextPlayer]++; CHANGED from player
			updateScore(nextPlayer);
		}
		removeActor(trumpsActor);
	}

	
	// get instace for factory LIZ TRY8IING TO MOVE PLAYER FACTORY 
	// private static Oh_Heaven instance = null;
	// public static Oh_Heaven getInstance(){
	// 	if(instance == null){
	// 		instance = new Oh_Heaven();

	// 	}
	// 	return instance;
	// }

	public Oh_Heaven(Properties properties) {
		super(700, 700, 30);
		setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");
		initConfig(properties);
	}

	public void playOh_Heaven() {
		initScores();
		initScore();
		for (int i=0; i < nbRounds; i++) {
			initTricks();
			initRound();
			playRound();
			updateScores();
		};
		for (int i=0; i <nbPlayers; i++) updateScore(i);
		int maxScore = 0;
		// fix score with getter from player -liz
		for (int i = 0; i <nbPlayers; i++) if (players[i].getScore() > maxScore) maxScore = players[i].getScore();
		Set <Integer> winners = new HashSet<Integer>();
		for (int i = 0; i <nbPlayers; i++) if (players[i].getScore() == maxScore) winners.add(i);
		String winText;
		if (winners.size() == 1) {
			winText = "Game over. Winner is player: " +
				winners.iterator().next();
		}
		else {
			winText = "Game Over. Drawn winners are players: " +
					String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
		}
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText(winText);
		refresh();
	}

	public void initConfig(Properties properties) {
		// Take parameters from properties file to initialize
		// Further optional parameters are described in the report
		if (properties != null) {
			seed = Integer.valueOf(properties.getProperty("seed"));
			nbStartCards = Integer.valueOf(properties.getProperty("nbStartCards"));
			nbRounds = Integer.valueOf(properties.getProperty("rounds"));
			enforceRules = Boolean.valueOf(properties.getProperty("enforceRules"));
			nbPlayers = 4;
			for (int i = 0; i < nbPlayers; i++) {
				players[i] = new Player(i, properties.getProperty("players." + i));
			}
		} else {
			seed = 30006;
			nbStartCards = 13;
			nbRounds = 2;
			enforceRules = false;
			nbPlayers = 4;
			players[0] = new Player(0, "human");
			for (int i = 1; i < nbPlayers; i++) {
				players[i] = new Player(i, "random");
			}
		}
		random = new Random(seed);
	}

	public static void main(String[] args) {
		// System.out.println("Working Directory = " + System.getProperty("user.dir"));
		final Properties properties;
		if (args == null || args.length == 0) {
			properties = PropertiesLoader.loadPropertiesFile(null);
		} else {
		    properties = PropertiesLoader.loadPropertiesFile(args[0]);
		}
		single_instance = new Oh_Heaven(properties);
		single_instance.playOh_Heaven();
	}

	public static Oh_Heaven getInstance() {
		return single_instance;
	}
}

