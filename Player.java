import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Player implements Comparable<Player> {
	private String name;
	private Queue<VehicleCard> deck = new ArrayDeque<>();
	
	public Player(final String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("invalid name");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		int sum = 0;
		for(VehicleCard i : deck) {
			sum += i.totalBonus();
		}
		
		return sum;
	}
	
	public void addCards(final Collection<VehicleCard> cards) {
		if (cards == null)
			throw new IllegalArgumentException("Player(addCards method): cards cannot be null");
		
		for (VehicleCard card : cards) {
			if (card == null)
				throw new IllegalArgumentException("Player(addCards method): card is invalid");
			else {
				deck.add(card);
			}
		}
	}
	
	public void addCard(final VehicleCard card) {
		if (card == null)
			throw new IllegalArgumentException("Player(addCard method): card cannot be null");
		deck.add(card);
	}
	
	public void clearDeck() {
		deck.clear();
	}
	
	public List<VehicleCard> getDeck() {
		List<VehicleCard> deckCopy = new ArrayList<>();
		deckCopy.addAll(deck);
		return deckCopy;
	}
	
	protected VehicleCard peekNextCard() {
		return deck.peek();
	}
	
	public VehicleCard playNextCard() {
		return deck.poll();
	}
	
	@Override
	public int compareTo(final Player other) {
		if (other == null)
			throw new IllegalArgumentException("Player(compareTo method): invalid name");		
		return this.name.compareToIgnoreCase(other.name);
	}
	
	@Override
	public int hashCode() {
		return this.name.toLowerCase().hashCode();
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Player other = (Player) obj;
		return Objects.equals(name.toLowerCase(), other.name.toLowerCase());
	}

	@Override
	public String toString() {
		String new_string = getName() + "(" + getScore() + "):\n";
		for (VehicleCard i : deck)
			new_string += i + "\n";
		return new_string.toString();		
	}
	
	public boolean challengePlayer(Player p) {
		VehicleCard vc1, vc2; 
		Queue<VehicleCard> vc1_newDeck = new ArrayDeque<VehicleCard>(), vc2_newDeck = new ArrayDeque<VehicleCard>();
		
		if (p == null || p.equals(this))
			throw new IllegalArgumentException("Player(ChallengePlayer method): Player cannot be equal to null or this");
		
		if (deck.isEmpty() || p.deck.isEmpty())
			return false;
		
		vc1 = this.playNextCard();
		vc2 = p.playNextCard();
		
		while (vc1.compareTo(vc2) == 0) {
			vc1_newDeck.add(vc1);
			vc2_newDeck.add(vc2);
			
			if (this.deck.isEmpty() || p.deck.isEmpty()) {
				this.deck.addAll(vc1_newDeck);
				p.deck.addAll(vc2_newDeck);
				return false;
			}
			
			vc1 = this.playNextCard();
			vc2 = p.playNextCard();
		}
		
		vc1_newDeck.add(vc1);
		vc2_newDeck.add(vc2);
		
		if (vc2.compareTo(vc1) > 0) {
			p.addCards(vc1_newDeck);
			p.addCards(vc2_newDeck);
			return false;
		}
		
		else {
			this.addCards(vc1_newDeck);
			this.addCards(vc2_newDeck);
			return true;
		}
	}
	
	public static Comparator<Player> compareByScore() {
		return (Player player1, Player player2) -> Integer.compare(player1.getScore(), player2.getScore());
	}
	
	public static Comparator<Player> compareByDeckSize() {
		return (Player player1, Player player2) -> Integer.compare(player1.getDeck().size(), player2.getDeck().size());
	}
}
