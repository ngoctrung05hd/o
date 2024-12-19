package atde.member;

import atde.components.*;
import atde.core.*;
import atde.gamelogic.Deck;

public class Player implements Member{
    private Hand hand;
    private String role;
    private boolean success;
    private boolean firstMove = false;
    private Deck deck;
    private boolean endTurn;
    private String name;
    private int id;

    public Player() {
        this.hand = new Hand();
        this.role = "";
        this.success = false;
        deck = null;
    }

    public void collect(Card card) {
        hand.add(card);
    }

    public void collect(CardList cardList) {
        hand.add(cardList);
    }

    public void getMove() {
    	boolean firstMove = (deck.getCardsUsed().size() == 0);
    	setSuccess(false);
        setFirstMove(firstMove);
    }

    public void attack(CardList attackCards) {
    	System.out.println("Attack: " + hand.CardListToString());
        setFirstMove(false);
        
        for (int i = 0; i < attackCards.size(); ++i) {
            hand.remove(attackCards.getCard(i));
        }
        deck.attack(attackCards);
    }

    public void defend(Card needToDefend, Card card) {
    	System.out.println("Defend: " + hand.CardListToString());
        hand.remove(card);    	
    	deck.defend(needToDefend, card);
        setSuccess(true);
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

    private void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public Hand getHand() {
        return hand;
    }

    public NeedToDefend getNeedToDefend() {
        return deck.getNeedToDefend();
    }

    public CardsUsed getCardsUsed() {
        return deck.getCardsUsed();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public int getId() {
    	return this.id;
    }

    public boolean isSuccess() {
        return success;
    }
}
