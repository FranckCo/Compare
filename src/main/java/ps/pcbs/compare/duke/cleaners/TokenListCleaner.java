package ps.pcbs.compare.duke.cleaners;

import java.util.Arrays;
import java.util.List;

import no.priv.garshol.duke.Cleaner;

/**
 * Removes all tokens from a given list passed to the constructor. Careful: no
 * internal space normalization is performed (but external spaces are trimmed).
 * 
 * @author Franck Cotton
 */
public class TokenListCleaner implements Cleaner {

	private String token = null;
	private List<String> toDelete = null;

	public TokenListCleaner() {
		super();
	}

	public TokenListCleaner(List<String> toDelete) {
		super();
		// Safer to make a copy
		// this.toDelete = new ArrayList<String>(toDelete.size());
		// Collections.copy(this.toDelete, toDelete); // TODO Fix that, does not
		// work
	}

	@Override
	public String clean(String value) {
		
//		System.out.println("value to clean" +value);
		
		for (String token : this.getToDelete())
			value = value.replace(token, "").replaceAll("\\s+", " ");

		
//		System.out.println("value cleaned "+value.trim());
		return value.trim();
	}

	public List<String> getToDelete() {
		this.toDelete = Arrays.asList(this.getToken().split(","));
		return toDelete;
	}

	public void setToDelete(List<String> toDelete) {
		this.toDelete = toDelete;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
