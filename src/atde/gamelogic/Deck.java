package atde.gamelogic;

import atde.components.*;
import atde.core.*;
import atde.member.*;
import atde.userinterface.CardGameController;

import java.util.ArrayList;

public class Deck implements base.Deck{
    private CardSet cardSet;
    private CardsUsed cardsUsed;
    private NeedToDefend needToDefend;
    private ArrayList<Member> members;
    private int currentMemberId;
    private int	startMemberId;
    private int endTurnCount = 0;
    private CardGameController controller;

    public Deck() {
        cardSet = new CardSet();
        cardsUsed = new CardsUsed();
        needToDefend = new NeedToDefend();
        members = new ArrayList<>();
        endTurnCount = 0;
    }

    public void reset() {
        cardSet = new CardSet();
        cardsUsed.reset();
        needToDefend.removeAll();

		for (Member mem : members) {
			mem.clearHand();
		}
    }
    
    public void drawCard() {
    	for (int i = 0; i < 8; ++i) {
    		for (Member mem : members) {
    			mem.collect(cardSet.getEnd());
    		}
    	}
    }

    public boolean checkAttack(Card card) {
        return cardsUsed.rankUsed(card);
    }

    public boolean checkAttack(CardList attackCards) {
        for (int i = 0; i < attackCards.size(); ++i) {
            if (!checkAttack(attackCards.getCard(i)))
                return false;
        }
        return true;
    }

    public boolean checkAttackFirstMove(CardList attackCards) {
        if (attackCards.size() <= 0)
            return false;
        for (int i = 0; i < attackCards.size(); ++i) {
            if (attackCards.getCard(0).getRank() != attackCards.getCard(i).getRank())
                return false;
        }
        return true;
    }

    public boolean checkDefend(Card needToDefendCard, Card card) {
        return card.checkDefend(needToDefendCard);
    }

    public boolean checkDefend(CardList needToDefendCards, CardList pickedCards) {
        return needToDefendCards.size() == 1 && pickedCards.size() == 1 && checkDefend(needToDefendCards.getFirstCard(), pickedCards.getFirstCard());
    }

    public void attack(CardList cardList) {
        needToDefend.add(cardList);
        cardsUsed.add(cardList);
        controller.display();
        endGame();
    }

    public void defend(Card needToDefendCard, Card card) {
        needToDefend.remove(needToDefendCard);
        cardsUsed.add(card);
        controller.display();

        endGame();
    }
    
    public void endTurn() {
    	if (members.get(currentMemberId).getRole().equals("attack")) {
    		if (currentMemberId == startMemberId && needToDefend.size() == 0) {
    			startMemberId = (startMemberId + 1) % members.size();
    			afterRound();
    			newRound();
    		}
    		else {
    			currentMemberId = (currentMemberId + 1) % members.size();
    			if (members.get(currentMemberId) instanceof Player) {
    				controller.setPlayer((Player) members.get(currentMemberId));
    			}
    			members.get(currentMemberId).getMove(); 
    		}
    	}
    	else {
    		if (members.get(currentMemberId).getSuccess() == false) {
    			members.get(currentMemberId).collect(getCardsUsed());
    			afterRound();
	    		startMemberId += 2;
	    		startMemberId %= members.size();
	    		newRound();
	    		try {
	                Thread.sleep(2000); // Dừng 2 giây
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	    		controller.display();
    		}
    		else {
        		currentMemberId = (currentMemberId + 1) % members.size();
    			if (members.get(currentMemberId) instanceof Player) {
    				controller.setPlayer((Player) members.get(currentMemberId));
    			}
            	members.get(currentMemberId).getMove(); 
    		}
    	}   	
    }
    
    private void endGame() {
    	if (members.get(currentMemberId).getHand().size() == 0) {
    		if (cardSet.size() == 0) {
    			System.out.println("Player " + members.get(currentMemberId) + " chien thang\n");
    			controller.setWinner(members.get(currentMemberId));
    			controller.endGamePane();
    		}
    		else {
    			int count = Math.min(cardSet.size(), 8);
    			try {
    	            Thread.sleep(2000);
    	        } catch (InterruptedException e) {
    	            e.printStackTrace();
    	        }
    			members.get(currentMemberId).collect(getCards(count));
    			controller.display();
    		}
    	}
    }
    
    private void afterRound() {
    	clearDeck();
		try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		reDraw();
		controller.display();
    }
    
    private void clearDeck() {
    	cardsUsed.reset();
    	if (needToDefend.size() > 0)
    		needToDefend.removeAll();
		controller.display();
    }
    
    private void reDraw() {
    	for (int i = 0; i < members.size(); ++i) {
    			int id = (startMemberId + i) % members.size();
    			int count = Math.min(cardSet.size(), Math.max(0, 8 - members.get(id).getHand().size()));
    			members.get(id).getHand().add(getCards(count));
   		}
		controller.display();
    }
    
    
    public void newRound() {
    	currentMemberId = startMemberId;
    	for (int i = 0; i < members.size(); ++i) {
    		members.get(i).setRole("attack");
    		if (i == (startMemberId + 1) % members.size()) 
    			members.get(i).setRole("defend");
    	}
    	if (controller != null) {
    		if (members.get(startMemberId) instanceof Player) {
    			controller.setPlayer((Player) members.get(startMemberId));
    		}
    		controller.display();
    	}
    	members.get(startMemberId).getMove();
    }
    
    public void newGame() {
    	reset();
    	drawCard();
    	setStartMemberId(0);
    	newRound();
    }

    private CardList getCards(int count) {
        CardList cardList = new CardList();
        while (count > 0 && cardSet.size() > 0) {
            cardList.add(cardSet.getEnd());
            count--;
        }
        return cardList;
    }

    public NeedToDefend getNeedToDefend() {
        return needToDefend;
    }

    public CardsUsed getCardsUsed() {
        return cardsUsed;
    }

    public CardSet getCardSet() {
        return cardSet;
    }

    public int getEndTurnCount() {
        return endTurnCount;
    }
    
    public void setStartMemberId(int startMemberId) {
    	this.startMemberId = startMemberId;
    }
    
    public int getStartMemberId() {
    	return startMemberId;
    }

    public void setEndTurnCount(int endTurnCount) {
        this.endTurnCount = endTurnCount;
    }

    public void setController(CardGameController controller) {
        this.controller = controller;
    }

    public void addMember(Member member) {
    	this.members.add(member);
    	member.setDeck(this);
    	member.setId(members.size() - 1);
    }
    
    public ArrayList <Member> getMembers() {
    	return members;
    }
    
    public ArrayList <Player> getPlayersList() {
    	ArrayList <Player> playersList = new ArrayList<>();
    	for (Member mem : members) {
    		if (mem instanceof Player) {
    			playersList.add((Player) mem);
    		}
    	}
    	return playersList;
    }
    
    public Player getPlayer(int index) {
    	while (members.get(index) instanceof BotEasy) {
    		index = (index + 1) % members.size();
    	}
    	Player p = (Player) members.get(index);
    	return p;
    }
    
    public Member getMember(int index) { 
    	Member p = members.get(index);
    	return p;
    }
}