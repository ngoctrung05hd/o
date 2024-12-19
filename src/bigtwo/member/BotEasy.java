package bigtwo.member;

import java.util.ArrayList;
import java.util.List;

import bigtwo.components.Hand;
import bigtwo.core.*;
import bigtwo.gamelogic.Deck;

public class BotEasy implements Member{
    private Hand hand;
    private String role;
    private boolean success;
    private boolean firstMove = false;
    private Deck deck;
    private boolean endTurn;
    private String name;
    private int id;

    public BotEasy() {
        this.hand = new Hand();
        this.role = "";
        deck = null;
    }

    public void collect(Card card) {
        hand.add(card);
    }

    public void collect(CardList cardList) {
        hand.add(cardList);
    }
    
    public void getMove() {
        setSuccess(false);
        boolean end = true;
        List<Card> list = getHand().getAll();
       
        int minCardsToPlay = (deck.getLastCardList().size() == 0) ? 1 : deck.getLastCardList().size();
        int maxCardsToPlay = (deck.getLastCardList().size() == 0) ? hand.size() : deck.getLastCardList().size();

        outer: for (int size = minCardsToPlay; size <= maxCardsToPlay; size++) {
            List<List<Card>> combinations = getCombinations(list, size);
            
            for (List<Card> combination : combinations) {
                CardList subHand = new CardList();
            	for(Card card : combination) {
                    subHand.add(card);
            	}
            	if(deck.checkMove(subHand)) {
            		setSuccess(true);
            		attack(subHand);
            		break outer;
            	}
            }
        }

        if (!getSuccess())
        	deck.endTurn();
    }
    
    private List<List<Card>> getCombinations(List<Card> list, int k) {
        List<List<Card>> result = new ArrayList<>();
        combine(list, k, 0, new ArrayList<>(), result);
        return result;
    }

    private void combine(List<Card> list, int k, int start, List<Card> current, List<List<Card>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < list.size(); i++) {
            current.add(list.get(i));
            combine(list, k, i + 1, current, result);
            current.remove(current.size() - 1); // Backtrack
        }
    }

    public void attack(CardList cardList) {
        for (int i = 0; i < cardList.size(); ++i)
            hand.remove(cardList.getCard(i));
        deck.move(cardList);
    }

    public void clearHand() {
        hand.removeAll();
    }


    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public int getId() {
    	return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setSuccess(boolean success) {
    	this.success = success;
    }
    
    public boolean getSuccess() {
    	return success;
    }
}
