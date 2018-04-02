import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Shuffle {
	
	private ArrayList<Integer> cards = new ArrayList<Integer>();
	//private HashMap<Integer,Double> config = new HashMap<Integer, Double>();
	private HashMap<ArrayList<Integer>,Integer> config = new HashMap<ArrayList<Integer>, Integer>();
	private HashSet<ArrayList<Integer>> prevShuffles = new HashSet<ArrayList<Integer>>();
	private Random rand = new Random();
	private long sampleSize = 40000;
	private int numCards = 10;
	
	public static void main(String[] args) {
		System.out.println("Beginning system.");
		Shuffle sh = new Shuffle();
		sh.run();
	}
	
	private void resetCards() {
		cards.removeAll(cards);
		for(int i = 1; i <= numCards; i++) {
			cards.add(i);
		}
	}
	
	public void run() {
		long counter = 0;
		double chiSquared = 0;
		resetCards();
		
		long permutations = 1;
		// get the total number of permutations
		for(int i = 1; i <= numCards; i++) {
			permutations *= i;
		}
		double frequency = (double) sampleSize / (double) permutations;

		while(counter < sampleSize) {
			//ArrayList<Integer> shuffled = computerShuffle();
			ArrayList<Integer> shuffled = dovetailShuffle();
			if(config.containsKey(shuffled)) {
				config.put(shuffled, config.get(shuffled) + 1);
			} else {
				config.put(shuffled, 1);
			}
			counter++;
			if(counter % 1000000 == 0) {
				System.out.println("at: " + counter);
			}
		}
		
		chiSquared = getChiSquared(permutations, frequency);
        //chiSquared /= frequency;
        System.out.println("chi-squared = " + chiSquared);
	}
	
	private double getChiSquared(long permutations, double frequency) {
		long counter = 0;
		double x = 0;
		Iterator<ArrayList<Integer>> it = prevShuffles.iterator();
	    while(it.hasNext()){
	    	ArrayList<Integer> shuf = it.next();
	    	if(config.containsKey(shuf)) {
	    		x += Math.pow((config.get(shuf) - frequency), 2);
	    	}
	    	counter++;
	    }
	    long diff = permutations - counter;
	    x += diff*frequency*frequency;
	    x /= frequency;
	    return x;
	}
	
	private ArrayList<Integer> computerShuffle() {
		ArrayList<Integer> shuffled = new ArrayList<Integer>();
		for(int i = 0; i < numCards; i++) {
			int n = rand.nextInt(cards.size());
			shuffled.add(cards.get(n));
			cards.remove(n);
		}
		prevShuffles.add(shuffled);
		resetCards();
		return shuffled;
	}
	
	private ArrayList<Integer> dovetailShuffle() {
		ArrayList<Integer> shuffled = new ArrayList<Integer>();
		int half = numCards / 2;
		List<Integer> firstHalf = cards.subList(0, half);
		List<Integer> secondHalf = cards.subList(half, numCards);
		int firstIndex = 0;
		int secondIndex = 0;
		for(int i = 0; i < numCards; i++) {
			int n = rand.nextInt(2);
			if(n == 0 && (firstIndex != firstHalf.size() - 1)) {
				shuffled.add(firstHalf.get((firstIndex)));
				firstIndex++;
			} else if(n == 1 && (secondIndex != secondHalf.size() - 1)) {
				shuffled.add(secondHalf.get(secondIndex));
				secondIndex++;
			} else if(n == 0) {
				shuffled.add(secondHalf.get(secondIndex));
				secondIndex++;
			} else if(n == 1) {
				shuffled.add(firstHalf.get((firstIndex)));
				firstIndex++;
			}
		}
		prevShuffles.add(shuffled);
		return shuffled;
	}
}