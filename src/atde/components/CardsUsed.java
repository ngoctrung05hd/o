package atde.components;

import atde.core.*;

import java.util.ArrayList;
import java.util.Collections;

public class CardsUsed extends CardList {
    private ArrayList <Boolean> existRank;

    public CardsUsed() {
        super();
        this.existRank = new ArrayList<>(Collections.nCopies(Card.COUNT_RANK, Boolean.FALSE));
    }

    public void add(Card card) {
        super.add(card);
        existRank.set(card.getRank(), true);
        sort();
    }

    public void add(CardList cardList) {
        super.add(cardList);
        if (cardList.size() > 0)
            existRank.set(cardList.getCard(0).getRank(), true);
        sort();
    }
    
    public void resetExistRank() {
        Collections.fill(existRank, Boolean.FALSE);
    }

    public void reset() {
        removeAll();
        resetExistRank();
    }

    public boolean rankUsed(Card card) {
        return existRank.get(card.getRank());
    }
}
