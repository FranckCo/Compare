package ps.pcbs.compare.duke.cleaners;

import java.util.ArrayList;
import java.util.List;

import no.priv.garshol.duke.Cleaner;

/**
 * Constructs a <code>Cleaner</code> by chaining a list of <code>Cleaners</code>.
 * Functionally redundant with Duke's predefined ChainedCleaner.
 * 
 * @author Franck Cotton
 */
public class ChainCleaner implements Cleaner {

	List<Cleaner> cleaners = null;

	public ChainCleaner(List<String> cleanerNames) {

		this.cleaners = new ArrayList<Cleaner>();

		for (String cleanerName : cleanerNames) {

			try {
				@SuppressWarnings("unchecked")
				Cleaner cleaner = ((Class<Cleaner>) Class.forName(cleanerName)).newInstance(); // Check safer syntax
				this.cleaners.add(cleaner);
			} catch (ClassNotFoundException ignore) {
				// We just ignore the cleaner in case it is not found
			} catch (InstantiationException e) {
				// TODO See what happens here
			} catch (IllegalAccessException e) {
				// TODO Idem
			}
		}
	}

	@Override
	public String clean(String value) {

		if ((value == null) || (value.length() == 0)) return value;

		String result = value;
		for (Cleaner cleaner : cleaners) result = cleaner.clean(result);

		return result;
	}

}
