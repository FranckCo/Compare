package ps.pcbs.compare.duke.comparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.priv.garshol.duke.Comparator;
import ps.pcbs.compare.Config;

public class RatioComparator<T extends Comparator> implements Comparator {

	private String separator = Config.DEFAULT_TOKEN_SEPARATOR;

	public RatioComparator() {
		super();
	}

	@Override
	public boolean isTokenized() {
		// TODO Auto-generated method stub
		return false;
	}

	// TODO See if a more precise scoring is possible
	@Override
	public double compare(String v1, String v2) {

		List<String> list1 = new ArrayList<String>(Arrays.asList(v1.split("\\s"
				+ "|" + separator)));
		List<String> list2 = new ArrayList<String>(Arrays.asList(v2.split("\\s"
				+ "|" + separator)));

		int count = 0;
		double size = list1.size() + list2.size();

		for (String first : list1) {
			for (String second : list2) {
				if (first.equals(second)) {
					count++;
					list2.remove(second);
					break;
				}
			}
		}

		return (count / (count + (size - 2 * count)) > 1.0) ? 1.0 : count
				/ (count + (size - 2 * count));
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
