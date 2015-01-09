package ps.pcbs.compare.duke.comparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ps.pcbs.compare.Config;
import no.priv.garshol.duke.Comparator;

public class MultiComparator implements Comparator {

	private String separator = Config.DEFAULT_TOKEN_SEPARATOR;

	@Override
	public boolean isTokenized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double compare(String v1, String v2) {
		// TODO Auto-generated method stub

		List<String> list1 = new ArrayList<String>(Arrays.asList(v1
				.split(separator)));
		List<String> list2 = new ArrayList<String>(Arrays.asList(v2
				.split(separator)));
		list1.retainAll(list2);
		return  list1.size()>0 ? 1.0 : 0.0;
	}

}
