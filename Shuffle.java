import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Shuffle {
	
	private ArrayList<Integer> cards = new ArrayList<Integer>();
	private HashMap<Integer,Double> config = new HashMap<Integer, Double>();
	private Random rand = new Random();
	private long sampleSize = 20;
	private int numCards = 3;
	private double frequency = sampleSize / numCards;
	private double chiSquared;
	private int[] primes = {2,3,5};
	
	public static void main(String[] args) {
		System.out.println("Beginning system.");
		Shuffle sh = new Shuffle();
		sh.run();
	}
	
	private void resetCards() {
		cards.add(1);
		cards.add(2);
		cards.add(3);
	}
	
	public void run() {
		long counter = 0;
		resetCards();
		while(counter < sampleSize) {
			int tally = 1;
			for(int i = 0; i < numCards; i++) {
				int n = rand.nextInt(cards.size());
				tally *= Math.pow(primes[i],cards.get(n));
				cards.remove(n);
			}
			resetCards();
			if(config.containsKey(tally)) {
				// increment size by one
				config.put(tally, config.get(tally) + 1);
			} else {
				config.put(tally, 1.0);
			}
			counter++;
		}
		
        permute(java.util.Arrays.asList(1,2,3), 0);
        chiSquared /= frequency;
        System.out.println("chi-squared = " + chiSquared);
	}
	
	
	private void permute(java.util.List<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            //System.out.println(java.util.Arrays.toString(arr.toArray()));
            int tally = 1;
            for(int i = 0; i < arr.size(); i++) {
            	tally *= Math.pow(primes[i], arr.get(i));
            }
            //System.out.println("tally: " + tally);
            if(config.containsKey(tally)) {
				chiSquared += Math.pow((config.get(tally) - frequency),2);
            }
        }
    }
    
}