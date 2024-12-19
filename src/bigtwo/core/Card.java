package bigtwo.core;

import java.util.Objects;

public class Card extends base.Card implements Comparable<Card> {
    private static final int[] POINT = {11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public Card(int rank, int suit) {
        super(rank, suit);
    }

    public int[] POINT() {
        return POINT;
    }

    public int compareTo(Card card) {
        if (POINT()[this.getRank()] > POINT()[card.getRank()]) {
            return 1;
        } else if (POINT()[this.getRank()] < POINT()[card.getRank()]) {
            return -1;
        }
        else return Integer.compare(this.getSuit(), card.getSuit());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return this.getRank() == card.getRank() && this.getSuit() == card.getSuit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRank(), getSuit());
    }
}