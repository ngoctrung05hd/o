package atde.components;

import atde.core.*;

public class NeedToDefend extends CardList {
    public NeedToDefend() {
        super();
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
