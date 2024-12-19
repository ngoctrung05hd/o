package base;

public abstract class Card {
    static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    static final char[] SUITS = {'♠', '♣', '♦', '♥'};
    static public final int COUNT_RANK = 13;
    static public final int COUNT_SUITS = 4;
    protected int rank;
    protected int suit;
    protected abstract int[] POINT();

    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String CardToString() {
        return "" + RANKS[getRank()] + SUITS[getSuit()];
    }

    public String CardToLink() {
        String linkImage = "file:src/image/card/";
        if (RANKS[getRank()].equals("10")) {
            linkImage += "t";
        }
        else
            linkImage += RANKS[getRank()];
        switch (getSuit()) {
            case 0: linkImage += "s"; break;
            case 1: linkImage += "c"; break;
            case 2: linkImage += "d"; break;
            case 3: linkImage += "h"; break;
            default:
        }

        return linkImage + ".gif";
    }

    public int getPoint() {
        return POINT()[getRank()];
    }

    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }
}