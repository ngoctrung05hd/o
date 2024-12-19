package bigtwo.components;

import java.util.List;

import bigtwo.core.Card;
import bigtwo.core.CardList;

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
