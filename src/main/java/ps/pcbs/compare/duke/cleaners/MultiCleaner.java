package ps.pcbs.compare.duke.cleaners;

import ps.pcbs.compare.Config;
import no.priv.garshol.duke.Cleaner;

public class MultiCleaner<T extends Cleaner> implements Cleaner {

	private String baseCleaner = null;
	private String separator = Config.DEFAULT_TOKEN_SEPARATOR;
	private Cleaner Cleaner = null;

	@Override
	public String clean(String value) {

		String[] tokens = value.split(separator);

		StringBuilder builder = new StringBuilder();
		 for (String token : tokens)
		 builder.append(this.getCleaner().clean(token)).append(this.getSeparator());
		if(builder.length()>0){
		if (builder.substring(builder.length() - 1).equals(separator))
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public Cleaner getCleaner() {
//		try {
//			Class<?> cleanerClass = Class.forName(this.baseCleaner);
//			this.Cleaner = (Cleaner) cleanerClass.newInstance();
//		} catch (Exception e) {
//			throw new IllegalArgumentException("Invalid class name "
//					+ baseCleaner);
//		}
		return Cleaner;
	}

	
	 public void setCleaner(Cleaner cleaner) {
	 this.Cleaner = cleaner;
	 }

	public void setBaseCleaner(String baseCleaner) {
		this.baseCleaner = baseCleaner;

	}

	public String getBaseCleaner() {
		return baseCleaner;
	}

}
