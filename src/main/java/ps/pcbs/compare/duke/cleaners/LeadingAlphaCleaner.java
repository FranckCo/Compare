package ps.pcbs.compare.duke.cleaners;

import no.priv.garshol.duke.Cleaner;

/**
 * Removes all text before the first digit. Leaves the text unchanged if there is no digit.
 * 
 * @author Franck Cotton
 */
public class LeadingAlphaCleaner implements Cleaner {

	@Override
	public String clean(String value) {

		if ((value == null) || (value.length() == 0)) return value;

		for (int index = 0; index < value.length(); index++) {
			if (Character.isDigit(value.codePointAt(index))) return value.substring(index);
		}

		return value;
	}
}
