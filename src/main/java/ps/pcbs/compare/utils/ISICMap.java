package ps.pcbs.compare.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ps.pcbs.compare.Config;

public class ISICMap {
	
	
	private Map<String,String> IsicCorrespondance;
	private String csvFile="C:/Users/lenovo/Compare/files/csv/isic4_2013.csv";
	
	public Map<String, String> getIsicCorrespondance() {
		return IsicCorrespondance;
	}

	public void setIsicCorrespondance(Map<String, String> isicCorrespondance) {
		IsicCorrespondance = isicCorrespondance;
	}

	public ISICMap() {
		// TODO Auto-generated constructor stub
	}
	
	public void loadMap(){
		this.IsicCorrespondance = new HashMap<String,String>();
		
		String line;
		String header = null;
		int nbLinesRead=1;
		String[] lineSplitted;
		try (BufferedReader reader = new BufferedReader(
				new FileReader(csvFile))) {
			if(reader.readLine() != null) header=reader.readLine();

			while ((line = reader.readLine()) != null) {
				lineSplitted=line.split(Config.DEFAULT_SEPARATOR);
				if(lineSplitted[2].replace("\"", "").length()<5) {
					String toto="0"+lineSplitted[2].replace("\"", "");
					this.IsicCorrespondance.put(toto.trim(), lineSplitted[1].replace("\"", "").trim());
				}
				else this.IsicCorrespondance.put(lineSplitted[2].replace("\"", "").trim(), lineSplitted[1].replace("\"", "").trim());
				//System.out.println(lineSplitted[1]);
//				System.out.println(lineSplitted[2]);
			}
		} catch (IOException e) {e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws IOException {
		ISICMap map =new ISICMap();
		map.loadMap();
		;
		
		for (Entry<String, String> entry : map.getIsicCorrespondance().entrySet()) {
			System.out.println(" ISIC CODE " +entry.getKey() + " ISIC DESCRIPTION "+entry.getValue());

		}
	}
	
}
