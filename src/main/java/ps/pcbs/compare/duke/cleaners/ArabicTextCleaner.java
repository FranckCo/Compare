package ps.pcbs.compare.duke.cleaners;

import no.priv.garshol.duke.Cleaner;

public class ArabicTextCleaner implements Cleaner {

	public ArabicTextCleaner() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String clean(String value) {

		String result = value.replaceAll("أ|إ|آ", "ا");
		//String alif = value.replaceAll("أ", "ا");

		result = result.replaceAll("لآ|لأ|لإ", "لا");
		result = result.replaceAll("ة", "ه");

		result = result.replaceAll("كفر ", "كفر-");
		result = result.replaceAll("عبد ", "عبد-");

		return result;
	}

}
