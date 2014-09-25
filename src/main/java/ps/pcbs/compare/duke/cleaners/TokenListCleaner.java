package ps.pcbs.compare.duke.cleaners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.priv.garshol.duke.Cleaner;

/**
 * Removes all tokens from a given list passed to the constructor.
 * Careful: no internal space normalization is performed (but external spaces are trimmed).
 * 
 * @author Franck Cotton
 */
public class TokenListCleaner implements Cleaner {

	private List<String> toDelete = null;

	public TokenListCleaner(List<String> toDelete) {

		// Safer to make a copy
		this.toDelete = new ArrayList<String>(toDelete.size());
		Collections.copy(this.toDelete, toDelete); // TODO Fix that, does not work
	}

	@Override
	public String clean(String value) {

		for (String token : toDelete) value = value.replace(token, "");

		return value.trim();
	}
}
