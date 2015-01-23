package ps.pcbs.compare.duke.comparators;

import no.priv.garshol.duke.Comparator;

public class SuffixComparator implements Comparator {

	private int suffixLength = 0;

	public SuffixComparator() {
		super();
	}

	public SuffixComparator(int suffixLength) {
		super();
		this.suffixLength = suffixLength;
	}

	@Override
	public boolean isTokenized() {
		// TODO Auto-generated method stub
		return false;
	}

	// TODO See if a more precise scoring is possible
	@Override
	public double compare(String v1, String v2) {

		if ((v1 == null) || (v2 == null)) return (v1 == v2 ? 1.0 : 0.0);

		int start = v2.length() - suffixLength;

		if (start < 0) return (v1.equals(v2) ? 1.0 : 0.0);

		return (v1.endsWith(v2.substring(start)) ? 1.0 : 0.0);

	}

}
