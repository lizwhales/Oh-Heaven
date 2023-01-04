# Oh-Heaven
A card game called "Oh Heaven".

![image](https://user-images.githubusercontent.com/70874436/210529459-41af2b62-a0bb-495a-952e-df6e22e90ae9.png)


## Description of the Game
The Oh Heaven game play is briefly described below.  Note that some of the elements listed below as specific 
values are configurable.  
 
1. Oh Heaven is played with a standard fifty-two card deck with four suits and thirteen ranks in each suit. 
2. The game involves four players who play independently, i.e. there are no teams. You can assume exactly 
four players. The aim is to get the highest score. 
3. The game is made up of three rounds. Each round is made up of the following sequence: 
a. A hand of thirteen cards being dealt to each player, a trump suit is randomly selected (displayed 
in the upper left of Figure 1) for the round, and a player being randomly selected to bid first and 
then start or lead. 
b. Each player then makes a bid as to how many tricks they think they can win. They will get 10 
extra points if they make exactly this many tricks. The total bid cannot equal the thirteen, that is, 
the last player must bid such that it is not possible for every player to achieve their bid.  
c. The game play then proceeds as follows: 
i. The player taking the lead can play any card they wish from their hand to the centre; 
this card provides the basis for a trick. 
ii. Play proceeds clockwise from the lead with each player playing one card in turn to 
follow the lead. 
iii. Following players must play a card of the same suit as that lead if they have one.  If 
not, they may play any card they wish. 
iv. Once every player has played one card, the winner is the player who has played the 
highest card of the trump suit if any, or the highest card of the lead suit if not. 
v. The winner will receive one point for winning the trick; if any cards remain, they go 
back to step i by leading a card to start a new trick. 
b. At the end of the round, any player who made exactly the number of tricks that they bid will 
receive an additional 10 points. 
4. At the end of all rounds, the player (or players) with the highest score wins. 
 
The Oh Heaven program currently supports two types of players: human interactive players who select the card to 
play through a double-left-mouse-click, and one type of NPC, random players who select a card to play from their 
hand randomly without regard for the rules. However, it always has one human and three random players. 


## Enhancements Added
 
1. Additional NPC types are required immediately.  Others might be added in the future. Note that here 
“legal” means consistent with the rules. 
a. Legal: plays a random selection from all cards that can legally be played. 
b. Smart: a player that records all relevant information and makes a reasonable, legal choice based 
on that information. A Smart player must produce smarter play than a legal player and have the 
necessary information available in a suitable form so that an extremely good NPC player could be 
developed based on the Smart player. A Smart player should assume that other players are 
playing legally. 
2. Parameters: currently all elements of Oh Heaven are set in the code. The system should be made more 
configurable through a property file.
3. A report commenting on how the design could be extended to support NPCs making smarter bids.  
 
Note that, despite the graphics in Figure 1, an NPC must not be able to see another player’s card until they use it to 
lead or follow in the game play.  Players must not share information directly and must store their own information 
about the game they are playing, i.e. they must not access a common pool.  As well as cards played, a player can 
see which player played the card, the number of tricks taken by a player, and the scores. Note that inferred 
information is also useful, for example, it can be assumed when a player doesn’t follow suit, that the player must 
have no remaining cards in that suit. 
