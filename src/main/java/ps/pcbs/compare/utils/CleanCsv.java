package ps.pcbs.compare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ps.pcbs.compare.Config;
import ps.pcbs.compare.duke.cleaners.PhoneCleaner;

public class CleanCsv {

	public static void main(String[] args) throws IOException {
		logger.info("début du cleaning du fichier : " + Config.CLEAN);
		List<String[]> lines = new ArrayList<String[]>();

		try (BufferedReader br = new BufferedReader(
				new FileReader(Config.CLEAN))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] values = sCurrentLine.split("\t");

				lines.add(values);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("le fichier contient : " + lines.size() + " lignes");
		ArrayList<Integer> l = new ArrayList<Integer>();
		logger.info("les colonnes à cleaner sont les colonnes : ");
		for (int i = 0; i < args.length; i++) {
			l.add(Integer.parseInt(args[i]));
			logger.info(Integer.parseInt(args[i]) + " ");
		}
		writeCSVData(lines, l);
	}

	private static PhoneCleaner phoneCleaner = new PhoneCleaner();

	/**
	 * Log4J logger.
	 */
	private static final Logger logger = Logger.getLogger(CleanCsv.class);

	private static void writeCSVData(List<String[]> lines,
			List<Integer> columnToClean) throws IOException {
		// OutputStreamWriter writer= new OutputStreamWriter(new
		// CSVWriter csvWriter = new CSVWriter(writer, '#');

		PrintStream outStream = null;
		try {
			outStream = new PrintStream(new File(Config.CLEANOUT));
		} catch (FileNotFoundException e) {
			logger.fatal("Error opening output file: " + e.getMessage());
		}

		toStringArray(lines, outStream, columnToClean);

	}

	private static void toStringArray(List<String[]> lines, PrintStream p,
			List<Integer> columnToClean) {

		int cptLignescleaned = 0;
		;
		Iterator<String[]> it = lines.iterator();
		String[] header = it.next();
		for (int i = 0; i < header.length; i++) {

			p.print(header[i]);
			p.print("\t");
			if (columnToClean.contains(i)) {
				p.print(Config.DEFAULT_DELIMITER);
				p.print(header[i].replaceAll("\"", "") + "_Clean");
				p.print(Config.DEFAULT_DELIMITER);
				p.print(Config.DEFAULT_SEPARATOR);
			}
		}
		p.println("");
		while (it.hasNext()) {

			String[] line = it.next();

			for (int i = 0; i < line.length; i++) {

				// p.print("\"");
				p.print(line[i]);
				// p.print("\"");
				p.print("\t");
				if (columnToClean.contains(i)) {
					p.print("\"");
					p.print(phoneCleaner.clean(line[i].replaceAll("\"", "")));
					p.print("\"");
					p.print("\t");
					if (line[i] != null
							&& !line[i].replaceAll("\"", "").equals(
									phoneCleaner.clean(line[i].replaceAll("\"",
											"")))) {

						cptLignescleaned++;
					}

				}

			}

			p.println("");
		}
		logger.info("nombre d'obseravtions cleaned " + cptLignescleaned);

	}

}
