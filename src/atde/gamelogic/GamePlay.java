package atde.gamelogic;

import java.util.List;

import atde.member.*;

public class GamePlay {
    private int deckId;
    private Deck deck;
    
    public GamePlay() {
        deckId = -1;
        deck = new Deck();
    }
    
    public Deck getDeck() {
    	return deck;
    }

    public void main(List<String> playerName, int botNum ) {
        for (int i = 0; i < playerName.size(); i++ ) {
	        Player player = new Player();
	        player.setName(playerName.get(i));
	        deck.addMember(player);
        }
        
		for (int i = 0; i < botNum; i++) {
			BotEasy bot = new BotEasy();
			bot.setName("Bot " + (i + 1));
			deck.addMember(bot);
		}

        
        deck.newGame();
    }
}
