package atde.member;

import atde.components.Hand;
import atde.core.*;
import atde.gamelogic.Deck;

public interface Member extends base.Member {
    void collect(Card card);
    void collect(CardList cardList);
    Hand getHand();
    void setDeck(Deck deck);
    boolean getSuccess();
}
