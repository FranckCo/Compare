package ps.pcbs.compare.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ps.pcbs.compare.Config;

/**
 * 
 * @author Karreem class to Extract from DUke Output multiple matches in order
 *         to try to analize them and improve accuracy of the match
 *
 */
public class ExtractMultipleMatches {

	private static final Logger logger = Logger
			.getLogger(ExtractMultipleMatches.class);
	private String csvSplitBy=Config.DEFAULT_SEPARATOR;

	public String getCsvSplitBy() {
		return csvSplitBy;
	}

	public void setCsvSplitBy(String csvSplitBy) {
		this.csvSplitBy = csvSplitBy;
	}

	public static void main(String[] args) {

		ExtractMultipleMatches obj = new ExtractMultipleMatches();
		String csvFile = Config.BILAN;
		// Map <String,Integer> linestokeep=obj.run(csvFile,18);
		// obj.keep(csvFile,linestokeep,18);
		// Map <String,Integer> linestokeep=obj.run(csvFile,11);
		// obj.keep(csvFile,linestokeep,11);
		Map<String, Integer> linestokeep = obj.run(csvFile, 18);
		obj.keep(csvFile, linestokeep, 18);
		// // Map <String,Integer> linestokeep=obj.run(csvFile,8);
		// obj.keep(csvFile,linestokeep,8);

	}

	/**
	 * This method will create a tsv file containing only the multiple cases in
	 * the same directory but with the term -multiple added to the name of the
	 * csv file
	 * 
	 * @param csvFile
	 *            : the csv File from which we want to know the records matched
	 *            and the occurences for each of them
	 * @param linestokeep
	 *            : a map containing for each match : his origin line number in
	 *            the input file and the number of match with census
	 * @param index
	 *            : the index of the colum containing the line number which can
	 *            be considered like an internal id
	 */
	private void keep(String csvFile, Map<String, Integer> linestokeep,
			int index) {
		BufferedReader br = null;
		String line = "";
		List<String> lines = new ArrayList<String>();
		String header = null;

		try {

			br = new BufferedReader(new FileReader(csvFile));

			header = br.readLine();
			// System.out.println(header);
			while ((line = br.readLine()) != null) {

				String[] match = line.split(this.getCsvSplitBy());
				if (linestokeep.get(match[index]) != null) {
					if (linestokeep.get(match[index]) > 1)
						lines.add(line);
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(
				csvFile.replace("-", "-multiple-")))) {
			writer.write(header);
			writer.newLine();
			for (String unique : lines) {
				writer.write(unique);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("end of the filtering");

	}

	/**
	 * 
	 * @param csvFile
	 *            : the csv File from which we want to know the records matched
	 *            and the occurences for each of them
	 * @param index
	 *            : the index of the colum containing the line number which can
	 *            be considered like an internal id
	 * @return a map containing for each match : his origin line number in the
	 *         input file and the number of match with census
	 */

	public Map<String, Integer> run(String csvFile, int index) {

		BufferedReader br = null;
		String line = "";

		Map<String, Integer> maps = new HashMap<String, Integer>();

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				String[] match = line.split(this.getCsvSplitBy());
				if (maps.get(match[index]) != null) {
					int value = maps.get(match[index]);
					// System.out.println(match[index]);
					maps.put(match[index], value + 1);
				} else
					maps.put(match[index], 1);

			}
			int compteur = 0;
			for (Entry<String, Integer> entry : maps.entrySet()) {
				if (entry.getValue() > 1) {
					// System.out.println("municipality line "+entry.getKey());
					// System.out.println(" number of apparition "+entry.getValue());
					compteur = compteur + 1;
				}

			}
			logger.info("number of multiple cases " + compteur);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return maps;
	}

}