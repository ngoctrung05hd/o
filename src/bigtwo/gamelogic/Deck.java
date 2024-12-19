package bigtwo.gamelogic;

import bigtwo.components.*;
import bigtwo.core.*;
import bigtwo.member.*;
import bigtwo.userinterface.CardGameController;

import java.util.ArrayList;

public class Deck implements base.Deck{
    private CardSet cardSet;
    private LastCardList lastCardList;
    private ArrayList<Member> members;
    private int currentMemberId;
    private int	startMemberId;
    private int endTurnCount = 0;
    private int lastMemberId;
    private CardGameController controller;

    public Deck() {
        cardSet = new CardSet();
        lastCardList = new LastCardList();
        members = new ArrayList<>();
        endTurnCount = 0;
    }

    public void reset() {
        cardSet = new CardSet();
        lastCardList.removeAll();

		for (Member mem : members) {
			mem.clearHand();
		}
    }
    
    public void drawCard() {
    	for (int i = 0; i < 13; ++i) {
    		for (Member mem : members) {
    			mem.collect(cardSet.getEnd());
    		}
    	}
    }

    public boolean checkMove(CardList cardList) {
    	if (lastCardList.size() == 0) {
    		return (cardList.isValid());
    	}
    	if (cardList.getCardType() == lastCardList.getCardType() && 
    		cardList.size() == lastCardList.size() &&
   			cardList.getLastCard().compareTo(lastCardList.getLastCard()) > 0) {
    		return true;
    	}
    	else if (lastCardList.size() == 1 && lastCardList.getCard(0).POINT()[lastCardList.getCard(0).getRank()] == 12 && cardList.canKillTwo()) {
    		return true;
    	}
        return false;
    }

    public void move(CardList cardList) {
    	startMemberId = currentMemberId;
    	lastMemberId = currentMemberId;
        lastCardList.update(cardList);

        if (controller != null)
        {
        	System.out.println(cardList.CardListToString());
        	controller.display();
        }
        
        endGame();

        endTurn();        
    }
    
    public void endTurn() {
		currentMemberId = (currentMemberId + 1) % members.size();
		if (currentMemberId == lastMemberId) {
			newRound();
		}
		else 
		{
			if (members.get(currentMemberId) instanceof Player) {
				controller.setPlayer((Player) members.get(currentMemberId));
			}
			members.get(currentMemberId).getMove();
		}
    }
    
    private void endGame() {
    	if (members.get(currentMemberId).getHand().size() == 0) {
    		System.out.println("Player " + members.get(currentMemberId) + " chien thang\n");
    		controller.display();
    		controller.setWinner(members.get(currentMemberId));
    		controller.endGamePane();
    	}
    }
    
    private void afterRound() {
    	clearDeck();

		controller.display();
    }
    
    private void clearDeck() {
    	lastCardList.removeAll();
    	reset();
		controller.display();
    }
    
    public void newRound() {
    	currentMemberId = startMemberId;
        lastCardList.removeAll();
    	
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

    public LastCardList getLastCardList() {
        return lastCardList;
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
    
    public CardGameController getController() {
    	return controller;
    }
    
    public Member getMember(int index) { 
    	Member p = members.get(index);
    	return p;
    }
}
