package bigtwo;

import bigtwo.core.*;
import bigtwo.gamelogic.Deck;
import bigtwo.member.*;
import bigtwo.userinterface.CardGameGUI;

public class TestB {

	static public void main(String args[]) {
    	Deck deck = new Deck();
    	Player player = new Player();
    	Player bot = new Player();
    	deck.addMember(player);
    	deck.addMember(bot);
    	deck.newGame();
    	CardGameGUI.GUI(deck);
	}
}
