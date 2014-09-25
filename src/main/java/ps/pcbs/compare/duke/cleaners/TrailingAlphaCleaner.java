package ps.pcbs.compare.duke.cleaners;

import no.priv.garshol.duke.Cleaner;

/**
 * Removes all text after the last digit. Leaves the text unchanged if there is no digit.
 * 
 * @author Franck Cotton
 */
public class TrailingAlphaCleaner implements Cleaner {

	@Override
	public String clean(String value) {

		if ((value == null) || (value.length() == 0)) return value;

		for (int index = value.length() - 1; index >= 0; index--) {
			if (Character.isDigit(value.codePointAt(index))) return value.substring(0, index + 1);
		}

		return value;
	}
}
