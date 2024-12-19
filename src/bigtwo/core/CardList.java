package bigtwo.core;

import java.util.ArrayList;
import java.util.*;

public class CardList extends base.CardList<Card> {
    public CardList() {
        super();
    }

    public Card getFirstCard() {
        if (size() > 0)
            return (Card) cards.get(0);
        return null;
    }
    
    public Card getLastCard() {
        if (size() > 0)
            return (Card) cards.get(size() - 1);
    	return null;
    }
    
    String typeOfList() {
    	String type = "";
    	
    	return type;
    }
    
    private enum CardType {
		SINGLE, 
		PAIR, 
		TRIPLE, 
		FOUR_OF_A_KIND, 
		THREE_PAIRS, 
		FOUR_PAIRS, 
		STRAIGHT, 
		INVALID
	}
    
    public boolean isValid() {
    	return getCardType() != CardType.INVALID;
    }
    
    public boolean canKillTwo() {
    	return (getCardType() == CardType.THREE_PAIRS || getCardType() == CardType.FOUR_PAIRS || getCardType() == CardType.FOUR_OF_A_KIND);
    }

	private CardType determineType() {
		int size = this.size();
		if (size == 0)
			return CardType.INVALID;

		Map<Integer, Integer> valueCount = countCardValues(this.getAll());

		switch (size) {
		case 1:
			return CardType.SINGLE;
		case 2:
			return isPair(valueCount) ? CardType.PAIR : CardType.INVALID;
		case 3:
			if (isTriple(valueCount))
				return CardType.TRIPLE;
			if (isConsecutive())
				return CardType.STRAIGHT;
			return CardType.INVALID;
		case 4:
			if (isFourOfAKind(valueCount))
				return CardType.FOUR_OF_A_KIND;
			if (isConsecutive())
				return CardType.STRAIGHT;
			return CardType.INVALID;
		default:
			if (isConsecutive())
				return CardType.STRAIGHT;
			if (isThreePairs(valueCount))
				return CardType.THREE_PAIRS;
			if (isFourPairs(valueCount))
				return CardType.FOUR_PAIRS;
			return CardType.INVALID;
		}
	}

	private Map<Integer, Integer> countCardValues(List<Card> list) {
		Map<Integer, Integer> valueCount = new HashMap<>();
		for (Card card : list) {
			int value = card.getRank();
			valueCount.put(value, valueCount.getOrDefault(value, 0) + 1);
		}
		return valueCount;
	}

	private boolean isPair(Map<Integer, Integer> valueCount) {
		return valueCount.containsValue(2);
	}

	private boolean isTriple(Map<Integer, Integer> valueCount) {
		return valueCount.containsValue(3);
	}

	private boolean isFourOfAKind(Map<Integer, Integer> valueCount) {
		return valueCount.containsValue(4);
	}

	private boolean isConsecutive() {
		this.sort();

		for (Card card : this.getAll()) {
			if (card.POINT()[card.getRank()] == 12) {
				return false;
			}
		}
		
		for (int i = 1; i < this.size(); i++) {
			int prevPoint = this.getCard(i - 1).POINT()[this.getCard(i - 1).getRank()];
			int currPoint = this.getCard(i).POINT()[this.getCard(i).getRank()];

			if (currPoint - prevPoint != 1) {
				return false;
			}
		}

		return true;
	}

	private boolean isThreePairs(Map<Integer, Integer> valueCount) {
		List<Integer> pairs = new ArrayList<>();

		for (Card card : this.getAll()) {
			if (card.POINT()[card.getRank()] == 12) {
				return false;
			}
		}

		for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
			if (entry.getValue() == 2) {
				pairs.add(entry.getKey());
			}
		}

		if (pairs.size() != 3)
			return false;

		List<Integer> pointPairs = new ArrayList<>();
		for (int rank : pairs) {
			pointPairs.add(this.getAll().get(0).POINT()[rank]);
		}
		Collections.sort(pointPairs);

		return (pointPairs.get(1) - pointPairs.get(0) == 1) && (pointPairs.get(2) - pointPairs.get(1) == 1);
	}

	private boolean isFourPairs(Map<Integer, Integer> valueCount) {
		List<Integer> pairs = new ArrayList<>();

		for (Card card : this.getAll()) {
			if (card.POINT()[card.getRank()] == 12) {
				return false;
			}
		}

		for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
			if (entry.getValue() == 2) {
				pairs.add(entry.getKey());
			}
		}

		if (pairs.size() != 4)
			return false;

		List<Integer> pointPairs = new ArrayList<>();
		for (int rank : pairs) {
			pointPairs.add(this.getAll().get(0).POINT()[rank]);
		}
		Collections.sort(pointPairs);

		return (pointPairs.get(1) - pointPairs.get(0) == 1) && (pointPairs.get(2) - pointPairs.get(1) == 1)
				&& (pointPairs.get(3) - pointPairs.get(2) == 1);
	}

	public CardType getCardType() {
		return determineType();
	}
}
