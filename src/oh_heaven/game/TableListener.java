package oh_heaven.game;

import ch.aplu.jcardgame.*;

public interface TableListener {
    // Observer interface to be used by CardObserver
    public void onCardPlay(int playerNum, Oh_Heaven.Suit lead, Card selected);
}
