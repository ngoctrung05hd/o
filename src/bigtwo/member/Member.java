package bigtwo.member;

import bigtwo.components.Hand;
import bigtwo.core.*;
import bigtwo.gamelogic.Deck;

public interface Member extends base.Member {
    void collect(Card card);
    void collect(CardList cardList);
    Hand getHand();
    void setDeck(Deck deck);
    boolean getSuccess();
}
