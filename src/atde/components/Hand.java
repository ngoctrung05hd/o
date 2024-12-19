package atde.components;

import atde.core.*;

import java.util.List;

public class Hand extends CardList {
    public Hand() {
        super();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void add(Card card) {
        super.add(card);
        sort();
    }

    public void add(CardList cardList) {
        super.add(cardList);
        sort();
    }
}
