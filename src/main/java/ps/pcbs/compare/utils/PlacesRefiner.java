package ps.pcbs.compare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import no.priv.garshol.duke.Cleaner;

import org.apache.log4j.Logger;

import ps.pcbs.compare.Configuration;
import ps.pcbs.compare.duke.cleaners.TokenListCleaner;
import ps.pcbs.compare.duke.cleaners.TrailingAlphaCleaner;

/**
 * This class is dedicated to the treatment of the list of places (http://www.zinnar.pna.ps/addressServer/)
 * 
 * @author Franck Cotton
 */
public class PlacesRefiner {

	public static void main(String[] args) throws IOException {

		PlacesRefiner refiner = new PlacesRefiner();
		refiner.refine();
	}

	/**
	 * Logger Log4J.
	 */
	private static final Logger logger = Logger.getLogger(PlacesRefiner.class);

	/**
	 * Reads the file, applies the treatments and outputs the results.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param columnIndex Zero-based index of the column containing the values to count.
	 * @throws FileNotFoundException 
	 */
	void refine() throws IOException {

		logger.debug("Trying to open files " + Configuration.PLACES_RAW + " and " + Configuration.PLACES);

		// For the prefix in the quarter name, and the final letter in the basin number
		Cleaner cleanerQuarter = new TokenListCleaner(Arrays.asList("حي", "حوض", "حى"));
		Cleaner cleanerBasin = new TrailingAlphaCleaner();

		BufferedReader placesReader = new BufferedReader(new FileReader(new File(Configuration.PLACES_RAW)));
		PrintStream placesWriter = new PrintStream(new File(Configuration.PLACES), "UTF-8");
		String lineIn = placesReader.readLine(); // First line is column names
		placesWriter.print("Postal code\tTown/village\tQuarter number\tQuarter name\tBasin number");
		while ((lineIn = placesReader.readLine()) != null) {
			String[] components = lineIn.split("\t");
			// The 'Postal code' and 'Town/village' are left unchanged
			placesWriter.println();
			placesWriter.print(components[0] + "\t" + components[1] + "\t");
			// Remove the unwanted prefixes in the quarter name
			String quarterName = cleanerQuarter.clean(components[2]);
			// Then separate the number from the rest (if there is a rest)
			if (quarterName.indexOf(" ") > 0) placesWriter.print(quarterName.replaceFirst(" ", "\t"));
			else placesWriter.print(quarterName + "\t");
			// For the Basin number, we drop the final letter
			placesWriter.print("\t" + cleanerBasin.clean(components[3]));
		}

		try {
			placesReader.close();
			placesWriter.close();
		} catch (Exception ignored) {}
	}
}
