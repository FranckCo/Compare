package ps.pcbs.compare.duke.comparators;

import no.priv.garshol.duke.Comparator;
import ps.pcbs.compare.Config;

public class DoubleWordCountComparator implements Comparator {

	private String separator = Config.DEFAULT_TOKEN_SEPARATOR;

	private WordCountComparator firstComparator = null;
	private WordCountComparator secondComparator = null;

	public DoubleWordCountComparator() {
		super();
		firstComparator = new WordCountComparator();
		secondComparator = new WordCountComparator();
	}

	public DoubleWordCountComparator(int firstThreshold, int secondThreshold) {
		super();
		firstComparator = new WordCountComparator(firstThreshold);
		secondComparator = new WordCountComparator(secondThreshold);
	}

	@Override
	public boolean isTokenized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double compare(String v1, String v2) {

		// Format expected is v1 and v2 sequences of two names separated by the separator
		// TODO Secure limit cases

		if ((v1 == null) || (v2 == null)) return (v1 == v2 ? 1.0 : 0.0); // TODO Validate this
		
		String[] sequence1 = v1.split(separator);
		String[] sequence2 = v2.split(separator);
		
				if(sequence1.length>1 && sequence2.length>1)
		return (firstComparator.compare(sequence1[0], sequence2[0]) + secondComparator.compare(sequence1[1], sequence2[1])) / 2;
		else return 0;

	}

	public WordCountComparator getFirstComparator() {
		return firstComparator;
	}

	public void setFirstComparator(WordCountComparator firstComparator) {
		this.firstComparator = firstComparator;
	}

	public WordCountComparator getSecondComparator() {
		return secondComparator;
	}

	public void setSecondComparator(WordCountComparator secondComparator) {
		this.secondComparator = secondComparator;
	}
}
