package ps.pcbs.compare.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ps.pcbs.compare.Config;

public class ISIC4To3 {
	private Map<String, Pair> IsicCorrespondance;
	private String csvFile = "src/main/csv/isic3.csv";

	public Map<String, Pair> getIsicCorrespondance() {
		return IsicCorrespondance;
	}

	public void setIsicCorrespondance(Map<String, Pair> isicCorrespondance) {
		IsicCorrespondance = isicCorrespondance;
	}

	public ISIC4To3() {
		// TODO Auto-generated constructor stub
	}

	public void loadMap() {
		this.IsicCorrespondance = new HashMap<String, Pair>();
		Pair<String, String> isic3CodeToText;
		String line;
		String header = null;
		int nbLinesRead = 1;
		String[] lineSplitted;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(csvFile), "Windows-1256"))) {
			if (reader.readLine() != null)
				header = reader.readLine();
			while ((line = reader.readLine()) != null) {
				lineSplitted = line.split(Config.CSV_SEPARATOR);
				String isic4;
				String isic3;
				String labelIsic3;
				labelIsic3 = lineSplitted[2].trim();
				if (lineSplitted[0].replace("\"", "").length() < 5
						&& lineSplitted[1].replace("\"", "").length() < 5) {
					isic4 = "0" + lineSplitted[0].replace("\"", "").trim();
					isic3 = "0" + lineSplitted[1].replace("\"", "").trim();
				} else {
					isic4 = lineSplitted[0].replace("\"", "").trim();
					isic3 = lineSplitted[1].replace("\"", "").trim();
				}
				try {
					if (this.getIsicCorrespondance().get(isic4).getRight()
							.toString() != null
							&& !this.getIsicCorrespondance().get(isic4)
									.getRight().toString().isEmpty()) {

						isic3 = this.getIsicCorrespondance().get(isic4)
								.getLeft().toString()
								+ Config.DEFAULT_TOKEN_SEPARATOR + isic3;
						labelIsic3 = this.getIsicCorrespondance().get(isic4)
								.getRight().toString()
								+ Config.DEFAULT_TOKEN_SEPARATOR + labelIsic3;
					}
				} catch (NullPointerException e) {
				}
				isic3CodeToText = new Pair<String, String>(isic3, labelIsic3);
				this.IsicCorrespondance.put(isic4.trim(), isic3CodeToText);
				// else
				// this.IsicCorrespondance.put(
				// lineSplitted[2].replace("\"", "").trim(),
				// lineSplitted[1].replace("\"", "").trim());
				// // System.out.println(lineSplitted[1]);
				// // System.out.println(lineSplitted[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		ISIC4To3 map = new ISIC4To3();
		map.loadMap();
		;
		for (Entry<String, Pair> entry : map.getIsicCorrespondance().entrySet()) {
			System.out.println(" ISIC 4 CODE " + entry.getKey()
					+ " ISIC 3 CODE " + entry.getValue().getLeft().toString()
					+ " ISIC 3 DESCRIPTION "
					+ entry.getValue().getRight().toString());
		}
	}
}