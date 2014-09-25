package ps.pcbs.compare.duke.cleaners;

import org.apache.commons.lang3.StringUtils;

import no.priv.garshol.duke.Cleaner;

/**
 * Cleans the ISIC code found in the Chamber of Commerce files.
 * 
 * @author Franck Cotton
 */
public class RamallahNameCleaner implements Cleaner {

	@Override
	public String clean(String value) {

		if (value == null) return null;

		// We eliminate the '*' and '%' characters, and sequences of '-'
		String trimmed = value.trim();
		if (StringUtils.repeat('-', trimmed.length()).equals(trimmed)) return null;
		return StringUtils.strip(trimmed, "*%");
	}
}
