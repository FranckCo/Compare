package ps.pcbs.compare.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ps.pcbs.compare.Configuration;

/**
 * Utility class containing different methods for exporting Excel data to CSV files.
 * 
 * @author Franck Cotton
 */
public class ExcelExporter {

	public static void main(String[] args) throws IOException {

		ExcelExporter exporter = new ExcelExporter(Configuration.RAMALLAH, "src/test/resources/new-ramallah-name.txt");
		exporter.exportColumns(2, 0, true, Arrays.asList(1, 3));
		//ExcelExporter exporter = new ExcelExporter(Configuration.ISIC, "src/test/resources/isic-5.xml");
		//exporter.exportISICSolr(5, 5);
	}

	private String bookPath = null;
	private String csvPath = null;
	private String separator = Configuration.DEFAULT_SEPARATOR;
	private String delimiter = Configuration.DEFAULT_DELIMITER;

	private int maxLines = 0;

	/**
	 * Logger Log4J.
	 */
	private static final Logger logger = Logger.getLogger(ExcelExporter.class);

	/**
	 * Constructs an instance of <code>ExcelExporter</code> with given paths for input and output.
	 * 
	 * @param excelFilePath Path to the Excel spreadsheet from which to export.
	 * @param textFilePath Path to the text file where data will be written.
	 */
	private ExcelExporter(String excelFilePath, String textFilePath) {

		if ((excelFilePath == null) || (excelFilePath.length() == 0)) throw new IllegalArgumentException("Excel file path must be provided");
		if ((textFilePath == null) || (textFilePath.length() == 0)) throw new IllegalArgumentException("Output file path must be provided");

		bookPath = excelFilePath;
		csvPath = textFilePath;
	}

	/**
	 * Reads the Excel file and exports it to the CSV file, optionally adding line numbers and/or hashes.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param lineNumbers If true, the line numbers will be added in the CSV files.
	 * @param hash If true, a SHA-1 hash of the records will be added in the CSV files.
	 */
	void export(int sheetIndex, int firstLineIndex, boolean title, boolean lineNumbers, boolean hash) {

		logger.debug("Trying to open Excel file " + bookPath);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			FileInputStream inputStream = new FileInputStream(new File(bookPath));
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		// Opening output file
		PrintStream outStream = null;
		try {
			outStream = new PrintStream(new File(csvPath));
		} catch (FileNotFoundException e) {
			logger.fatal("Error opening output file: " + e.getMessage());
		}

		logger.debug("Beginning to read sheet " + data.getSheetName());

		MessageDigest digester = DigestUtils.getSha1Digest();
		DataFormatter formatter = new DataFormatter();
		Iterator<Row> rows = data.rowIterator ();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, write column names
		if (title && rows.hasNext()) {
			Row titles = rows.next();
			if (lineNumbers) outStream.print(delimiter + "Line number" + delimiter + separator);
			if (hash) outStream.print(delimiter + "Record hash" + delimiter + separator);
			Iterator<Cell> cells = titles.cellIterator();
			StringBuilder builder = new StringBuilder();
			while (cells.hasNext()) {
				String cellValue = formatter.formatCellValue(cells.next()).replaceAll("\\s+", " ").trim();
				builder.append(delimiter).append(cellValue).append(delimiter).append(separator);
			}
			// Delete last separator
			builder.deleteCharAt(builder.length() - 1);
			outStream.println(builder);
		}

		int rowNumber = 1;
		while (rows.hasNext()) {

			Row row = rows.next();

			Iterator<Cell> cells = row.cellIterator();
			StringBuilder builder = new StringBuilder();
			
			// We can insert a filter here
			//if (!formatter.formatCellValue(row.getCell(2)).equals("301810")) continue;

			while (cells.hasNext()) {
				Cell cell = cells.next();
				String cellValue = formatter.formatCellValue(cell).replaceAll("\\s+", " ");
				builder.append(delimiter).append(cellValue).append(delimiter).append(separator);
				if (hash) digester.update(cellValue.getBytes());
			}
			// Delete last separator
			builder.deleteCharAt(builder.length() - 1);
			if (rowNumber > 1) outStream.println();

			if (lineNumbers) outStream.print(delimiter + rowNumber + delimiter + separator);
			if (hash) outStream.print(delimiter + Hex.encodeHexString(digester.digest()) + delimiter + separator);
			outStream.print(builder);

			// For test purposes
			if (rowNumber == maxLines) break;
			rowNumber++;
		}
		logger.debug("Closing output file, " + rowNumber + " lines written");
		outStream.close();
	}

	/**
	 * Reads the Excel file and exports selected columns to the CSV file.
	 * 
	 * @param sheetIndex Zero-based index of the sheet containing the data.
	 * @param firstLineIndex Zero-based index of the first line to read in the sheet.
	 * @param title Indicates if the first line to read contains the column names.
	 * @param columnIndexes A list of integers giving the (zero-based) indexes of the columns to export.
	 */
	void exportColumns(int sheetIndex, int firstLineIndex, boolean title, List<Integer> columnIndexes) {

		logger.debug("Trying to open Excel file " + bookPath);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			FileInputStream inputStream = new FileInputStream(new File(bookPath));
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			data = wb.getSheetAt(sheetIndex);
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		// Opening output file
		PrintStream outStream = null;
		try {
			outStream = new PrintStream(new File(csvPath));
		} catch (FileNotFoundException e) {
			logger.fatal("Error opening output file: " + e.getMessage());
		}

		logger.debug("Beginning to read sheet " + data.getSheetName());

		DataFormatter formatter = new DataFormatter();
		Iterator<Row> rows = data.rowIterator ();

		// Skip first lines if necessary
		if (firstLineIndex > 0) while (rows.hasNext() && rows.next().getRowNum() < firstLineIndex - 1);

		// If first line is titles, write column names
		if (title && rows.hasNext()) {
			Row titles = rows.next();
			Iterator<Cell> cells = titles.cellIterator();
			StringBuilder builder = new StringBuilder();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				if (columnIndexes.contains(cell.getColumnIndex())) {
					String cellValue = formatter.formatCellValue(cell).replaceAll("\\s+", " ").trim();
					builder.append(delimiter).append(cellValue).append(delimiter).append(separator);
				}
			}
			// Delete last separator
			if (builder.length() > 0) builder.deleteCharAt(builder.length() - separator.length());
			outStream.println(builder);
		}

		int rowNumber = 1;
		while (rows.hasNext()) {

			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			StringBuilder builder = new StringBuilder();
			
			// We can insert a filter here
			//if (!formatter.formatCellValue(row.getCell(2)).equals("301810")) continue;

			while (cells.hasNext()) {
				Cell cell = cells.next();
				if (columnIndexes.contains(cell.getColumnIndex())) {
					String cellValue = formatter.formatCellValue(cell).replaceAll("\\s+", " ");
					builder.append(delimiter).append(cellValue).append(delimiter).append(separator);
				}
			}
			// Delete last separator
			if (builder.length() > 0) builder.deleteCharAt(builder.length() - separator.length());
			if (rowNumber > 1) outStream.println();

			outStream.print(builder);

			// For test purposes
			if (rowNumber == maxLines) break;
			rowNumber++;
		}
		logger.debug("Closing output file, " + rowNumber + " lines written");
		outStream.close();
	}

	/**
	 * Specialized method for exporting the ISIC from the Excel file published on zinnar.ps.
	 * 
	 * @param minLength Minimum code length for which the information will be exported.
	 * @param maxLength Maximum code length for which the information will be exported.
	 */
	void exportISIC(int minLength, int maxLength) {

		logger.debug("Trying to open Excel file " + bookPath);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			FileInputStream inputStream = new FileInputStream(new File(bookPath));
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			data = wb.getSheetAt(0); // For this spreadsheet, data is on the first sheet
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		// Opening output file
		PrintStream outStream = null;
		try {
			outStream = new PrintStream(new File(csvPath));
		} catch (FileNotFoundException e) {
			logger.fatal("Error opening output file: " + e.getMessage());
		}

		logger.debug("Beginning to read sheet " + data.getSheetName());
		logger.debug("Selecting items with code length between " + minLength + " and " + maxLength);

		DataFormatter formatter = new DataFormatter();
		Iterator<Row> rows = data.rowIterator ();

		// Skip the first lines which contains column names
		if (rows.hasNext()) rows.next();
		outStream.println(delimiter + "Code" + delimiter + separator + delimiter + "Label" + delimiter);

		int rowNumber = 1;
		while (rows.hasNext()) {

			Row row = rows.next();

			String codeValue = formatter.formatCellValue(row.getCell(0)).replaceAll("\\s+", " ").trim();
			if ((codeValue.length() < minLength) || (codeValue.length() > maxLength)) continue;

			String labelValue = formatter.formatCellValue(row.getCell(1)).trim();
			outStream.println(delimiter + codeValue + delimiter + separator + delimiter + labelValue + delimiter);

			// For test purposes
			if (rowNumber == maxLines) break;
			rowNumber++;
		}
		logger.debug("Closing output file, " + rowNumber + " lines written");
		outStream.close();
	}

	/**
	 * Specialized method for exporting the ISIC from the Excel file published on zinnar.ps to a XML document ready to load in Solr.
	 * 
	 * @param minLength Minimum code length for which the information will be exported.
	 * @param maxLength Maximum code length for which the information will be exported.
	 * @throws IOException 
	 */
	void exportISICSolr(int minLength, int maxLength) throws IOException {

		logger.debug("Trying to open Excel file " + bookPath);

		XSSFSheet data = null;
		InputStream sourceFile = null;
		try {
			FileInputStream inputStream = new FileInputStream(new File(bookPath));
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			data = wb.getSheetAt(0); // For this spreadsheet, data is on the first sheet
		} catch (Exception e) {
			logger.fatal("Error opening Excel file: " + e.getMessage());
			return;
		} finally {
			if (sourceFile != null) try {sourceFile.close();} catch(Exception ignored) {}
		}

		// Opening output file
		PrintStream outStream = null;
		try {
			outStream = new PrintStream(new File(csvPath), "UTF-8");
		} catch (FileNotFoundException e) {
			logger.fatal("Error opening output file: " + e.getMessage());
		}

		logger.debug("Beginning to read sheet " + data.getSheetName());
		logger.debug("Selecting items with code length between " + minLength + " and " + maxLength);

		DataFormatter formatter = new DataFormatter();
		Iterator<Row> rows = data.rowIterator ();

		// Skip the first lines which contains column names
		if (rows.hasNext()) rows.next();
		outStream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		outStream.println("<add>");

		int rowNumber = 1;
		while (rows.hasNext()) {

			Row row = rows.next();

			String codeValue = formatter.formatCellValue(row.getCell(0)).replaceAll("\\s+", " ").trim();
			if ((codeValue.length() < minLength) || (codeValue.length() > maxLength)) continue;

			String labelValue = formatter.formatCellValue(row.getCell(1)).trim();
			outStream.println("\t<doc>");
			outStream.println("\t\t<field name=\"id\">" + codeValue + "</field>");
			outStream.println("\t\t<field name=\"name\">" + labelValue + "</field>");
			outStream.println("\t</doc>");

			rowNumber++;
		}
		outStream.print("</add>");

		logger.debug("Closing output file, " + rowNumber + " lines read");
		outStream.close();
	}

}
