package ps.pcbs.compare.duke.cleaners;

import no.priv.garshol.duke.Cleaner;

public class CommercialOwnerNameCleaner<T extends Cleaner> implements Cleaner {

	private TokenListCleaner tokenCleaner;
	private Cleaner arabicCleaner;

	public TokenListCleaner getTokenCleaner() {
		return tokenCleaner;
	}

	public void setTokenCleaner(TokenListCleaner tokenCleaner) {
		this.tokenCleaner = tokenCleaner;
	}


	public Cleaner getArabicCleaner() {
		return arabicCleaner;
	}

	public void setArabicCleaner(Cleaner arabicCleaner) {
		this.arabicCleaner = arabicCleaner;
	}

	public CommercialOwnerNameCleaner() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String clean(String value) {
		if (tokenCleaner != null) {
			
			value = tokenCleaner.clean(value);
		}
		if (arabicCleaner != null) {
			
			return arabicCleaner.clean(value);
		}
		return value;
	}

}
