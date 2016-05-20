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

		int maxLengthCom   = Math.max(Arrays.asList(commName1.split(" ")).size(),
				Arrays.asList(commName2.split(" ")).size());
		int maxLengthOw    = Math.max(Arrays.asList(ownName1.split(" ")).size(),
				Arrays.asList(ownName2.split(" ")).size());
		int maxLengthOwCom =  Math.max(Arrays.asList(ownName1.split(" ")).size(),
				Arrays.asList(commName2.split(" ")).size());
		int maxLengthComOw =  Math.max(Arrays.asList(commName1.split(" ")).size(),
				Arrays.asList(ownName2.split(" ")).size());			
		// If the number of common elements is equal || more to the threshold,

//		if (v1.equals(v2))
//			return 1.0;
		
		int ccom_CN=countCommonWords(commName1,commName2);
		int ccom_OW=countCommonWords(ownName1,ownName2);
		
		int ccom_OW_CN=countCommonWords(ownName1,commName2);
		int ccom_CN_OW=countCommonWords(commName1,ownName2);
		
		double comm_Score=0; 
		double own_Score=0;
		
		double final_Score=0;
		int scoreFin=0;
		

		// Cas MC>=5
		if (maxLengthCom >= 5) {
			if (ccom_CN>3){
				comm_Score=1;
				} else {
					if(ccom_CN==2 || ccom_CN==3){
						comm_Score=0.5;
					} else {
						comm_Score=0;
					}
				}
		} else {
			// Cas MC=3 ou 4 
			if (maxLengthCom==3 || maxLengthCom==4){
				if (ccom_CN>=3){
				comm_Score=1;
				} else {
					if(ccom_CN==2){
						comm_Score=0.5;
					} else {
						comm_Score=0;
					}
				}
			}
		}
		
		// Cas MO>=5
		if  (maxLengthOw>=5) { 
			if(ccom_OW>=3){ own_Score=1; } else { own_Score=0;}
		} 
		// Cas MO=3 ou 4
		if (maxLengthOw==3 || maxLengthOw==4){
			if (ccom_OW>=3){ own_Score = 1;	} else {
				if (ccom_OW==2) { own_Score =0.5;} else {own_Score = 0;}
			}
		} 
		// Cas où les deux Own_Name ont deux mots et deux mots en commun
		if (maxLengthOw==2 && ccom_OW==2) {own_Score=1;}
		
		
		final_Score = own_Score + comm_Score;
		
		if (final_Score>=1) {scoreFin=1;} else {
			// Sinon on compare en croisant
					
			  // Cas MO>=5
		if  (maxLengthOwCom >=5) { 
			if(ccom_OW_CN >=3){ own_Score=1; } else { own_Score=0;}
		} 
		// Cas MO=3 ou 4
		if (maxLengthOwCom ==3 || maxLengthOwCom ==4){
			if (ccom_OW_CN >=3){ own_Score = 1;	} else {
				if (ccom_OW_CN ==2) { own_Score =0.5;} else {own_Score = 0;}
			}
		} 
		// Cas où les deux Own_Name ont deux mots et deux mots en commun
		if (maxLengthOwCom==2 && ccom_OW_CN ==2) {own_Score=1;} 
		
			  // Cas MO>=5
		if  (maxLengthComOw >=5) { 
			if(ccom_CN_OW >=3){ comm_Score=1; } else { comm_Score=0;}
		}
		
		// Cas MO=3 ou 4
		if (maxLengthComOw ==3 || maxLengthComOw ==4){
			if (ccom_CN_OW >=3){ comm_Score = 1;	} else {
				if (ccom_CN_OW ==2) { comm_Score =0.5;} else {comm_Score = 0;}
			}
		} 
		// Cas où les deux Own_Name ont deux mots et deux mots en commun
		if (maxLengthComOw==2 && ccom_CN_OW ==2) {comm_Score=1;}
		
		final_Score = own_Score + comm_Score;
		
		if (final_Score>=1) {scoreFin=1;} else {scoreFin=0;}
		
		}
		return scoreFin;
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
