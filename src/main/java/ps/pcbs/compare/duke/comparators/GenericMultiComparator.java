package ps.pcbs.compare.duke.comparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.priv.garshol.duke.Comparator;
import ps.pcbs.compare.Config;

public class GenericMultiComparator<T extends Comparator> implements Comparator {

	private String separator = Config.DEFAULT_TOKEN_SEPARATOR;
	private Comparator comparator = null;
	private String baseComparator = null;

	public GenericMultiComparator() {
		super();
	}

	public GenericMultiComparator(Comparator comparator) {
		super();
		this.comparator = comparator;
	}

	public GenericMultiComparator(String comparatorClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super();
		this.setComparator(comparatorClassName);
	}

	@Override
	public boolean isTokenized() {
		// TODO Auto-generated method stub
		return false;
	}

	// TODO See if a more precise scoring is possible
	@Override
	public double compare(String v1, String v2) {

		List<String> list1 = new ArrayList<String>(Arrays.asList(v1.split(separator)));
		List<String> list2 = new ArrayList<String>(Arrays.asList(v2.split(separator)));
		double score = 0.0;

		for (String first : list1) {
			for (String second : list2) {
				score = this.getComparator().compare(first, second);
				if (score > 0.0) return score;
			}
		}

		return 0.0;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}
	
	public Comparator getComparator() {
		try {
			Class<?> comparatorClass = Class.forName(this.baseComparator);
			this.comparator = (Comparator) comparatorClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid class name "
					+ comparator);
		}
		return comparator;
	}


	public void setComparator(String comparatorClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		Class<?> comparatorClass = Class.forName(comparatorClassName);
		this.comparator = (Comparator) comparatorClass.newInstance();
	}
	
	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getBaseComparator() {
		return baseComparator;
	}

	public void setBaseComparator(String baseComparator) {
		this.baseComparator = baseComparator;
	}
}
