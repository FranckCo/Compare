package ps.pcbs.compare.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.priv.garshol.duke.Cleaner;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ps.pcbs.compare.Config;
import ps.pcbs.compare.Configuration;
import ps.pcbs.compare.duke.cleaners.PhoneCleaner;

public class DataHelper {

	public static void main(String[] args) throws IOException {

//		DataHelper helper = new DataHelper(Configuration.CENSUS);
//		helper.countTokens(0, 0, true, 8, null, Config.BilanToken);
		DataHelper helper = new DataHelper(Config.RAMALLAH);
		helper.countTokens(0, 0, true, 2, null, Config.BilanToken);
		// helper.count(0, 2, true, 1,
		// ps.pcbs.compare.duke.cleaners.LeadingAlphaCleaner.class);
		// helper.checkCodification(0, 0, true, 12, 13);
		// helper.countNullOrVoid(0, 0, true, 7);
		// helper.countEqual(0, 0, true, 6, 9);
		// helper.checkRegex(0, 0, true, 4, Configuration.RAMALLAH_PHONE_REGEX);
	}

	private String bookPath = null;
	private PhoneCleaner phoneCleaner = new PhoneCleaner();
	private File reportFile = null;
	private PrintStream printStream = null;

	/**
	 * Log4J logger.
	 */
	private static final Logger logger = Logger.getLogger(DataHelper.class);

	private DataHelper(String excelFilePath) {

		if ((excelFilePath == null) || (excelFilePath.length() == 0)) throw new IllegalArgumentException("Excel file path must be provided");
		bookPath = excelFilePath;
	}

	/**
	 * Counts the values of a column.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param columnIndex Zero-based index of the column containing the values to count.
	 * @param cleanerClass Cleaner A cleaner to apply to the column values.
	 */
	void count(int sheetIndex, int firstLineIndex, boolean title, int columnIndex, Class<? extends Cleaner> cleanerClass) {

		logger.debug("Trying to open Excel file " + bookPath);

		// Add a cleaner here
		Cleaner cleaner = null;
		try {
			cleaner = cleanerClass.newInstance();
		} catch (Exception ignored) {} // An NPE will occur later

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(bookPath)));
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		Map<String, Integer> counter = new HashMap<String, Integer>();

		logger.debug("Beginning to read sheet " + data.getSheetName());

		Iterator<Row> rows = data.rowIterator ();
		DataFormatter formatter = new DataFormatter();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, get the column name
		String variableName;
		if (title && rows.hasNext()) variableName = formatter.formatCellValue(rows.next().getCell(columnIndex));
		else variableName = "Column " + columnIndex;

		int rowNumber = 1;
		while (rows.hasNext()) {
			String value = formatter.formatCellValue((rows.next().getCell(columnIndex)));
			if (cleaner != null) value = cleaner.clean(value);
			if (counter.containsKey(value)) counter.put(value, counter.get(value) + 1);
			else counter.put(value, 1);
			rowNumber++;
		}
		// Report
		System.out.println(variableName);
		logger.info("Value counts for variable " + variableName);
		for (String value : counter.keySet()) {
			System.out.println(value + "\t" + counter.get(value));
			logger.info("Value: " + value + "\t\t\tCount: " + counter.get(value));
		}
		System.out.println("Number of lines: " + (rowNumber - 1));
	}

	/**
	 * Counts the frequencies of the text tokens in the (string) values of a column.
	 * 
	 * @param sheetIndex
	 *            Zero-based index of the sheet containing the data.
	 * @param firstLineIndex
	 *            Zero-based index of the first line to read in the sheet.
	 * @param title
	 *            Indicates if the first line to read contains the column names.
	 * @param columnIndex
	 *            Zero-based index of the column containing the values to count.
	 * @param cleanerClass
	 *            Cleaner A cleaner to apply to the column values.
	 * @param reprtFilePath
	 *           file in which we write results
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	void countTokens(int sheetIndex, int firstLineIndex, boolean title,
			int columnIndex, Class<? extends Cleaner> cleanerClass, String reportFilePath) throws FileNotFoundException, UnsupportedEncodingException {

		logger.debug("Trying to open Excel file " + bookPath);

		// Add a cleaner here
		Cleaner cleaner = null;
		try {
			cleaner = cleanerClass.newInstance();
		} catch (Exception ignored) {} // An NPE will occur later

		HSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(
					bookPath)));
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		Map<String, Integer> counter = new HashMap<String, Integer>();

		logger.debug("Beginning to read sheet " + data.getSheetName());

		Iterator<Row> rows = data.rowIterator ();
		DataFormatter formatter = new DataFormatter();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, get the column name
		String variableName;
		if (title && rows.hasNext())
			variableName = formatter.formatCellValue(rows.next().getCell(
					columnIndex));
		else
			variableName = "Column " + columnIndex;

		int rowNumber = 1;
		while (rows.hasNext()) {
			String value = formatter.formatCellValue((rows.next().getCell(columnIndex)));
			if (cleaner != null) value = cleaner.clean(value);
			String[] tokens = value.split("\\s+");
			for (String token : tokens) {
				if (counter.containsKey(token)) counter.put(token, counter.get(token) + 1);
				else counter.put(token, 1);				
			}
			rowNumber++;
			if (rowNumber % 1000 == 0) System.out.println("Last line read " + rowNumber);
		}
		// Report
		System.out.println(variableName);
		logger.info("Value counts for variable " + variableName);
		Map<String, Integer> sortedMap = sortByComparator(counter);
		if (reportFilePath== null)
			printStream = System.out;
		else {
			reportFile = new File(reportFilePath);
			printStream = new PrintStream(reportFile, "UTF-8");
		}
		for (String value : sortedMap.keySet()) {
//			System.out.println(value + "\t" + sortedMap.get(value));
			printStream.println(value + "\t" + sortedMap.get(value));
			// logger.info("Value: " + value + "\t\t\tCount: " +
			// counter.get(value));
		}
		System.out.println("Number of lines: " + (rowNumber - 1));
	}

	private Map<String, Integer> sortByComparator(Map<String, Integer> counter) {
		// Convert Map to List
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(
				counter.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it
				.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * Checks if a code in a column is always associated to the same label in another column.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param codeColumnIndex Zero-based index of the column containing the codes.
	 * @param labelColumnIndex Zero-based index of the column containing the labels.
	 */
	void checkCodification(int sheetIndex, int firstLineIndex, boolean title, int codeColumnIndex, int labelColumnIndex) {

		logger.debug("Trying to open Excel file " + bookPath);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(bookPath)));
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		Map<String, String> checker = new HashMap<String, String>();

		logger.debug("Beginning to read sheet " + data.getSheetName());

		Iterator<Row> rows = data.rowIterator ();
		DataFormatter formatter = new DataFormatter();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, get the column names
		String codeVariableName, labelVariableName;
		if (title && rows.hasNext()) {
			Row titles = rows.next();
			codeVariableName = formatter.formatCellValue(titles.getCell(codeColumnIndex));
			labelVariableName = formatter.formatCellValue(titles.getCell(labelColumnIndex));
		}
		else {
			codeVariableName = "Code " + codeColumnIndex;
			labelVariableName = "Label " + labelColumnIndex;
		}

		int rowNumber = 1;
		int differences = 0;
		while (rows.hasNext()) {
			Row row = rows.next();
			String codeValue = formatter.formatCellValue(row.getCell(codeColumnIndex));
			String labelValue = formatter.formatCellValue(row.getCell(labelColumnIndex));
			if (checker.containsKey(codeValue)) {
				if (!checker.get(codeValue).equals(labelValue)) {
					checker.put(codeValue, "*");
					differences++;
				}
			}
			else checker.put(codeValue, labelValue);
			rowNumber++;
		}
		// Report
		System.out.println("'" + codeVariableName + "' values with different '" + labelVariableName + " 'labels");
		for (String code : checker.keySet()) {
			if (checker.get(code).equals("*")) System.out.println(code);
		}
		System.out.println("Lines: " + (rowNumber - 1) + "\tCodes: " + checker.size() + "\tCodes with different labels: " + differences);

		for (String code : checker.keySet()) System.out.print(code + "\t");

	}

	/**
	 * Counts the values that are empty or 'NULL'.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param columnIndex Zero-based index of the column containing the values to count.
	 */
	void countNullOrVoid(int sheetIndex, int firstLineIndex, boolean title, int columnIndex) {

		logger.debug("Trying to open Excel file " + bookPath);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(bookPath)));
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		int numberVoid = 0;
		int numberNull = 0;

		logger.debug("Beginning to read sheet " + data.getSheetName());

		Iterator<Row> rows = data.rowIterator ();
		DataFormatter formatter = new DataFormatter();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, get the column name
		String variableName;
		if (title && rows.hasNext()) variableName = formatter.formatCellValue(rows.next().getCell(columnIndex));
		else variableName = "Code " + columnIndex;

		while (rows.hasNext()) {

			Row row = rows.next();
			String value = formatter.formatCellValue(row.getCell(columnIndex)).trim();
			// We can insert a filter here
			if (!formatter.formatCellValue(row.getCell(2)).equals("301810")) continue;

			if ((value.length() == 0) || (value.equals("-"))) numberVoid++;
			if (value.toUpperCase().equals("NULL")) numberNull++;
		}
		// Report
		System.out.println("Variable: " + variableName + "\nEmpty values: " + numberVoid + "\tNull values: " + numberNull);
	}

	/**
	 * Counts the values that are empty or 'NULL'.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param firstColumnIndex Zero-based index of the column containing the first value to compare.
	 * @param secondColumnIndex Zero-based index of the column containing the second value to compare.
	 */
	void countEqual(int sheetIndex, int firstLineIndex, boolean title, int firstColumnIndex, int secondColumnIndex) {

		logger.debug("Trying to open Excel file " + bookPath);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(bookPath)));
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		int numberEqual = 0;
		int numberDifferent = 0;

		logger.debug("Beginning to read sheet " + data.getSheetName());

		Iterator<Row> rows = data.rowIterator ();
		DataFormatter formatter = new DataFormatter();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, get the column name
		String firstVariableName, secondVariableName;
		if (title && rows.hasNext()) {
			Row titles = rows.next();
			firstVariableName = formatter.formatCellValue(titles.getCell(firstColumnIndex));
			secondVariableName = formatter.formatCellValue(titles.getCell(secondColumnIndex));
		}
		else {
			firstVariableName = "Column " + firstColumnIndex;
			secondVariableName = "Column " + secondColumnIndex;
		}

		while (rows.hasNext()) {
			Row row = rows.next();
			String firstValue = formatter.formatCellValue(row.getCell(firstColumnIndex)).trim();
			String secondValue = formatter.formatCellValue(row.getCell(secondColumnIndex)).trim();
			if (firstValue.equals(secondValue)) numberEqual++;
			else {
				System.out.println(firstValue + "\t" + secondValue);
				numberDifferent++;
			}
		}
		// Report
		System.out.println("Variables " + firstVariableName + " and " + secondVariableName);
		System.out.println("Equal values: " + numberEqual + "\tDifferent values: " + numberDifferent);
	}

	/**
	 * Verifies that a variable satisfies a regular expression.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param columnIndex Zero-based index of the column containing the first value to compare.
	 * @param regex Regular expression to text.
	 */
	void checkRegex(int sheetIndex, int firstLineIndex, boolean title, int columnIndex, String regex) {

		logger.debug("Trying to open Excel file " + bookPath);

		Pattern pattern = Pattern.compile(regex);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(bookPath)));
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		int empty = 0;
		int matches = 0;
		int partialMatches = 0;
		int noMatches = 0;

		logger.debug("Beginning to read sheet " + data.getSheetName());

		Iterator<Row> rows = data.rowIterator ();
		DataFormatter formatter = new DataFormatter();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, get the column name
		String variableName;
		if (title && rows.hasNext()) variableName = formatter.formatCellValue(rows.next().getCell(columnIndex));
		else variableName = "Column " + columnIndex;

		while (rows.hasNext()) {
			String value = formatter.formatCellValue(rows.next().getCell(columnIndex)).trim();

			if (value.isEmpty()) {
				empty++;
				continue;
			}
			Matcher matcher = pattern.matcher(phoneCleaner.clean(value));
			//Matcher matcher = pattern.matcher(value);

			if (matcher.matches()) matches++;
			else {
				if (matcher.find()) {
					System.out.println(value + "\tPartial match");
					partialMatches++;
				}
				else {
					System.out.println(value + "\tNo match");
					noMatches++;
				}
			}
		}
		// Report
		System.out.println("Variable " + variableName + "\tEmpty values: " + empty);
		System.out.println("Matches: " + matches + "\tPartial matches: " + partialMatches + "\tNo matches: " + noMatches);
	}
}
