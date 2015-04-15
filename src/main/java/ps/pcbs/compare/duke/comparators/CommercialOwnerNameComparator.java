package ps.pcbs.compare.duke.comparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ps.pcbs.compare.Config;
import no.priv.garshol.duke.Comparator;

public class CommercialOwnerNameComparator implements Comparator {

	private String separator = Config.DEFAULT_TOKEN_SEPARATOR;

	@Override
	public boolean isTokenized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double compare(String v1, String v2) {
		// TODO Auto-generated method stub
//		System.out.println(" first string "+v1);
//		System.out.println(" second string "+v2);
		
		if (isNull(v1) || isNull(v2))
			return (v1 == v2 ? 1.0 : 0.0); // TODO Validate this
		
		
		String[] sequence1 = v1.split(separator);
		String[] sequence2 = v2.split(separator);

		String commName1 = getCommercialName(sequence1);
		String commName2 = getCommercialName(sequence2);
		String ownName1 = getOwnerName(sequence1);
		String ownName2 = getOwnerName(sequence2);

		int maxLength = Math.max(Arrays.asList(commName1.split(" ")).size(),
				Arrays.asList(commName2.split(" ")).size());
		// If the number of common elements is equal or more to the threshold,

//		if (v1.equals(v2))
//			return 1.0;
		
		if(maxLength==3) return (countCommonWords(commName1, commName2)==3 ? 1.0: 0.0);
		if (maxLength > 5) {

			return (countCommonWords(commName1, commName2) / maxLength > 0.75 ? 1.0
					: 0.0);

		} else {
			if (countCommonWords(commName1, commName2) >= 3)
				return (countCommonWords(ownName1, ownName2) >= 1 ? 1.0 : 0.0);
			if (countCommonWords(commName1, commName2) >= 2)
				return (countCommonWords(ownName1, ownName2) >= 2 ? 1.0 : 0.0);
//			if (countCommonWords(commName1, commName2) >= 1)
//				return (countCommonWords(ownName1, ownName2) >= 3 ? 1.0 : 0.0);
		}
		return 0.0;
	}

	private String getOwnerName(String[] sequence1) {
	
		if (sequence1.length<2 || sequence1[1] == null)
			return "";
		return sequence1[1];
	}

	private String getCommercialName(String[] sequence1) {
		if (sequence1[0] == null)
			return "";
		return sequence1[0];
	}

	private boolean isNull(String v1) {
		if (v1 == null || v1.equals("") || v1.equals("#"))
			return true;
		return false;
	}

	private int countCommonWords(String stringName1, String stringName2) {
		Set<String> ISn1 = new HashSet<>();
		Set<String> ISn2 = new HashSet<>();
		List<String> listofStringName1 = Arrays.asList(stringName1.split(" "));
		for (String s : listofStringName1) {
			ISn1.add(s.trim());
		}
		List<String> listofStringName2 = Arrays.asList(stringName2.split(" "));
		for (String s : listofStringName2) {
			ISn2.add(s.trim());
		}

		ISn1.retainAll(ISn2);
		return ISn1.size();
	}

}
