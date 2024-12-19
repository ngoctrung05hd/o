package bigtwo.components;

import bigtwo.core.CardList;

public class LastCardList extends CardList {
    public LastCardList() {
        super();
    }

    public void update(CardList cardList) {
    	removeAll();
        super.add(cardList);
    }
}
