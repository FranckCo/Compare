package ps.pcbs.compare.duke.comparators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import no.priv.garshol.duke.Comparator;

public class WordCountComparator implements Comparator {

	private int threshold = 0;

	public WordCountComparator() {
		super();
	}

	public WordCountComparator(int threshold) {
		super();
		this.threshold = threshold;
	}

	@Override
	public boolean isTokenized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double compare(String v1, String v2) {

		if ((v1 == null) || (v2 == null)) return (v1 == v2 ? 1.0 : 0.0); // TODO Validate this

		// Validate this on cases where there are double occurrences of one term in one or both lists
		//List<String> list1 = new ArrayList<String>(Arrays.asList(v1.split(" "))); // TODO Split on any sequence of spaces
		//List<String> list2 = new ArrayList<String>(Arrays.asList(v2.split(" ")));

		Set<String> set1 = new HashSet<>(Arrays.asList(v1.split(" "))); // TODO Split on any sequence of spaces
		Set<String> set2 = new HashSet<>(Arrays.asList(v2.split(" ")));

		// If the number of common elements is equal or more to the threshold, return 1
		set1.retainAll(set2);
		return (set1.size() >= this.threshold ? 1.0 : 0.0);
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}
