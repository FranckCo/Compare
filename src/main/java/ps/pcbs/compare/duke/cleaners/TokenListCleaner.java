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

		for (String token : this.getToDelete())
			value = value.replace(token, "").replaceAll("\\s+", " ");

		if (value.contains("&") || value.contains("4")) {
			if (value.trim().substring(0, 2).matches("\\p{InArabic}+")) {
				value = value.replace("&", "و");
				if (value.contains("4"))
					value = " فور " + value.replace("4", "");
			} else {
				value = value.replace("&", "and");
				value = value.replace("4", "for");
			}
		}
		
		value = value.replace("أ", "ا").replace("إ", "ا").replace("إ", "ا")
				.replace("آ", "ا");
		value = value.replace("لأ", "لا").replace("لآ", "لا")
				.replace("لإ", "لا");
		value = value.replace("ابو ", "ابو").replace("أبو ", "أبو").replace("عبد ", "عبد")
				.replace("دير ", "دير").replace("بير ", "بير").replace("كفر ", "كفر")
				.replace("عين ", "عين").replace("نيو ", "نيو").replace("بيت ", "بيت");

		return value.trim().replaceAll("[^\\p{InArabic}&&[^\\p{L}]&&[^\\s]]", "").toLowerCase().trim();
	}

	public List<String> getToDelete() {
		this.toDelete = Arrays.asList(this.token.split(","));
		return toDelete;
	}

	public void setToDelete(List<String> toDelete) {
		this.toDelete = toDelete;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
