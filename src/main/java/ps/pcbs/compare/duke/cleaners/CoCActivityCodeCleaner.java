package ps.pcbs.compare.duke.cleaners;

import no.priv.garshol.duke.Cleaner;

/**
 * Cleans the ISIC code found in the Chamber of Commerce files.
 * 
 * @author Franck Cotton
 */
public class CoCActivityCodeCleaner implements Cleaner {

	@Override
	public String clean(String code) {

		if (code.length() == 3) return code + "0";
		if (code.length() == 4) {
			if (code.equals("3720")) return code;
			return "0" + code.substring(0, 3);
		}
		return code.substring(0, 4);
	}
}
