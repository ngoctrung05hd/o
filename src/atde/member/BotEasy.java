package atde.member;

import atde.components.*;
import atde.core.*;
import atde.gamelogic.Deck;

public class BotEasy implements Member, base.Bot {
    private Hand hand;
    private String role;
    private boolean success;
    private Deck deck;
    private boolean endTurn;
    private String name;
    private int id;
    
    public BotEasy() {
        this.hand = new Hand();
        this.role = "";
        this.success = false;
        this.deck = null;
    }

    @Override
    public void getMove() {
        try {
            Thread.sleep(2000); // Dừng 2 giây
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        hand.sort();
        setEndTurn(false);
        if (getRole().equals("attack")) {
        	boolean firstMove = (deck.getCardsUsed().size() == 0);
            attack(firstMove);
        }
        else {
            defend();
        }
        deck.endTurn();
    }

    public void collect(Card card) {
        hand.add(card);
    }

    public void collect(CardList cardList) {
        hand.add(cardList);
    }

    public void attack(boolean firstMove) {
        CardList attackCards = new CardList();
        if (firstMove)
        {
            for (int i = 0; i < hand.size(); ++i)
                if(hand.getFirstCard().getRank() == hand.getCard(i).getRank()) {
                    attackCards.add(hand.getCard(i));
                }

            for (int i = 0; i < attackCards.size(); ++i) {
                hand.remove(attackCards.getCard(i));
            }
        }
        else {
            boolean end = true;
            for (int i = 0; i < hand.size(); ++i){
                    if (deck.checkAttack(hand.getCard(i))) {
                    	Card pick = hand.getCard(i);
                        hand.remove(i);
                        attackCards.add(pick);
                        end = false;
                    }
                }

            if (deck.getNeedToDefend().size() == 0 && deck.getCardsUsed().size() > 0) {
                setEndTurn(end);
            }
        }
        deck.attack(attackCards);
    }

    public void defend() {
        setSuccess(false);
        NeedToDefend needToDefend = deck.getNeedToDefend();
        for (int i = 0; i < hand.size(); ++i) {
            for (int j = 0; j < needToDefend.size(); ++j) {
                if (deck.checkDefend(needToDefend.getCard(j), hand.getCard(i))) {
                    Card pick = hand.getCard(i);
                    hand.remove(i);
                    deck.defend(needToDefend.getCard(j), pick);
                    setSuccess(true);
                    return;
                }
            }
        }
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

    public boolean isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
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
}
