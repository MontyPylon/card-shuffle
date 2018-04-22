import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class NewShuffle {
	
	private ArrayList<Integer> cards = new ArrayList<Integer>();
	private HashMap<ArrayList<Integer>,Integer> config = new HashMap<ArrayList<Integer>, Integer>();
	//private HashSet<ArrayList<Integer>> prevShuffles = new HashSet<ArrayList<Integer>>();
	private long[][]places = new long[numCards][numCards];
	private Random rand = new Random();
	private long sampleSize = numCards * 10000;
	private static int numCards = 52;
	private double[] chiValues = new double[numCards];
	
	public static void main(String[] args) {
		NewShuffle sh = new NewShuffle();
		sh.run();
	}
	
	private void resetCards() {
		cards.removeAll(cards);
		for(int i = 0; i < numCards; i++) {
			cards.add(i);
		}
	}
	
	public void run() {
		long counter = 0;
		resetCards();
		double frequency = (double) sampleSize / (double) numCards;

		while(counter < sampleSize) {
			//ArrayList<Integer> shuffled = computerShuffle();
			ArrayList<Integer> shuffled = dovetailShuffle(cards);
			for(int i = 0; i < 9; i++) {
				shuffled = dovetailShuffle(shuffled);
			}
			//ArrayList<Integer> shuffled = mergeShuffle(cards);
			
			for(int i = 0; i < numCards; i++) {
				int placement = shuffled.indexOf(i);
				places[i][placement] = places[i][placement] + 1;
			}
			
			counter++;
			if(counter % 1000 == 0) {
				System.out.println("at: " + counter);
			}
		}
		
		setChiSquaredValues(frequency);
        //chiSquared /= frequency;
        System.out.println("Chi-squared values = " + Arrays.toString(chiValues) + "\n");
        for(int i = 0; i < numCards; i++) {
			System.out.println("Card " + i + ": " + Arrays.toString(places[i]));
        }
        double max = 0;
        for(int i = 0; i < numCards; i++) {
        	if(max < chiValues[i]) {
        		max = chiValues[i];
        	}
        }
        System.out.println("\nMax chi-squared value: " + max);
	}
	
	private void setChiSquaredValues(double frequency) {
		for(int i = 0; i < numCards; i++) {
			for(int j = 0; j < numCards; j++) {
				chiValues[i] = chiValues[i] + (Math.pow((places[i][j] - frequency),2) / frequency);
			}
		}
	}
	
	private ArrayList<Integer> computerShuffle() {
		ArrayList<Integer> shuffled = new ArrayList<Integer>();
		for(int i = 0; i < numCards; i++) {
			int n = rand.nextInt(cards.size());
			shuffled.add(cards.get(n));
			cards.remove(n);
		}
		//prevShuffles.add(shuffled);
		resetCards();
		return shuffled;
	}
	
	private ArrayList<Integer> dovetailShuffle(ArrayList<Integer> cards) {
		ArrayList<Integer> shuffled = new ArrayList<Integer>();
		int half = numCards/2;
		List<Integer> firstHalf = cards.subList(0, half);
		List<Integer> secondHalf = cards.subList(half, numCards);
		int firstIndex = 0;
		int secondIndex = 0;
		for(int i = 0; i < numCards; i++) {
			int n = rand.nextInt(2);
			if(n == 0 && (firstIndex <= firstHalf.size() - 1)) {
				shuffled.add(firstHalf.get((firstIndex)));
				firstIndex++;
			} else if(n == 1 && (secondIndex <= secondHalf.size() - 1)) {
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
		//prevShuffles.add(shuffled);
		return shuffled;
	}
	
	private ArrayList<Integer> mergeShuffle(ArrayList<Integer> cards){
		ArrayList<Integer> shuffled = new ArrayList<Integer>(cards.size());
		int order[] = new int[cards.size()];
		for (int i = 0;i<order.length;i++) {
			order[i]=i;
			shuffled.add(0);
		}
		mergeShuffleR(order,0,order.length);
		for(int i=0;i<cards.size();i++) {
			shuffled.set(i,cards.get(order[i]));
		}
		//prevShuffles.add(shuffled);
		return shuffled;
	}
	private void mergeShuffleR(int order[], int start, int finish){
		int size = finish-start;
		if (size <= 1) {
			return ;
		} else
		if (size == 2) {
			int temp = 0;
			temp = order[start];
			order[start]=order[start+1];
			order[start+1]=temp;
			return;
		} else {
		//swap 2 halves or not
		int r = rand.nextInt(2);
		int half = size/2;
		if (r==1) {
			for (int i = start; i < half; i++) {
				int temp = order[i];
				order[i] = order[i-start+half];
				order[i-start+half] = temp;
			}
		}
		mergeShuffleR(order, start,start+half);
		mergeShuffleR(order, start+half, finish);
		return;
		}
	}
}