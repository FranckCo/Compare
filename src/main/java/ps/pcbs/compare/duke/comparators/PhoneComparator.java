package ps.pcbs.compare.duke.comparators;

import no.priv.garshol.duke.Comparator;

/**
 * Comparator which compares two number phones by skipping two first figures. It
 * returns 1.0 if they are equal, and 0.0 if they are different.
 */
public class PhoneComparator implements Comparator {

	public boolean isTokenized() {
		return true;
	}

	public double compare(String v1, String v2) {
	
		if (v1.length()>2 && v2.length()>2){
			return v1.substring(2).equals(v2.substring(2)) ? 1.0 : 0.0;
		}
		else {
			return v1.equals(v2) ? 1.0 : 0.0;
		}

	}

}
