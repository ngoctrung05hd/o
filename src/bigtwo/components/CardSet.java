package bigtwo.components;

import java.util.Collections;

import bigtwo.core.Card;
import bigtwo.core.CardList;

public class CardSet extends CardList {
    public CardSet() {
        super();
        for (int _rank = 0; _rank < Card.COUNT_RANK; ++_rank)
            for (int _suit = 0; _suit < Card.COUNT_SUITS; ++_suit)
                    add(new Card(_rank, _suit));
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card getEnd() {
        Card back = null;
        if (this.size() > 0) {
            back = (Card) cards.getLast();
            cards.removeLast();
        }
        return back;
    }
}
