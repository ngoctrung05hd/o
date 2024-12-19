package atde.components;

import atde.core.*;

import java.util.Collections;

public class CardSet extends CardList {
    public CardSet() {
        super();
        for (int _rank = 0; _rank < Card.COUNT_RANK; ++_rank)
            for (int _suit = 0; _suit < Card.COUNT_SUITS; ++_suit)
                    add(new Card(_rank, _suit));
        shuffle();
        int specialSuit = getCard(0).getSuit();
        for (int i = 0; i < size(); i++) {
            Card card = (Card) getCard(i);
            card.setSpecialSuit(specialSuit);
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card getBegin() {
        Card begin = null;
        if (this.size() > 0)
            begin = cards.getFirst();
        return begin;
    }

    public Card getEnd() {
        Card back = null;
        if (this.size() > 0) {
            back = cards.getLast();
            cards.removeLast();
        }
        return back;
    }
}
