package ps.pcbs.compare.duke.listeners;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.matchers.MatchListener;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Reports match events and non match events as text or XML to the console or in separate files.
 * 
 * @author
 */
public class FileProcessListener implements MatchListener {

	/** Delimiter to use for text output */
	private String delimiter = "\"";
	/** Field separator to use for text output */
	private String separator = "\t";
	/** Value separator to use for text output */
	private String valueSeparator = "|";

	private File reportFileMatch = null;
	private File reportFileUnMatch = null;
	private PrintStream printStreamMatch = null;
	private PrintStream printStreamUnMatch = null;
	private int[] counts = { 0, 0, 0 };
	private boolean[] matchTypes = { false, false, false };
	private boolean xmlOutput = false;
	private boolean emptyValues = false;
	private Map<Integer, List<String>> propertyNames = null;

	private static final Logger logger = Logger
			.getLogger(FileProcessListener.class); // TODO Switch to Log4J 2

	/**
	 * Constructs a <code>FileMatchListener</code> with the given properties.
	 * 
	 * @param reportFilePath
	 *            Path to the report file (if null, reports to console, if ends
	 *            with '.xml', report will be in XML).
	 * @param configurationFilePath
	 *            Path to the Duke matching configuration file.
	 * @param reportMatches
	 *            <code>boolean</code> indicating if matches should be reported.
	 * @param reportPossibleMatches
	 *            <code>boolean</code> indicating if possible matches should be
	 *            reported.
	 * @param reportNonMatches
	 *            <code>boolean</code> indicating if non matches should be
	 *            reported.
	 * @param writeEmptyValues
	 *            <code>boolean</code> indicating if empty property values
	 *            should be written (mainly relevant for XML output).
	 * @throws Exception
	 */
	public FileProcessListener(String reportFilePath,
			String configurationFilePath, boolean reportMatches,
			boolean reportPossibleMatches, boolean reportNonMatches,
			boolean writeEmptyValues) throws Exception {

		if (reportFilePath == null)
			printStreamMatch = printStreamUnMatch = System.out;
		else {
			reportFileMatch = new File(reportFilePath);
			reportFileUnMatch = new File(
					reportFilePath.replace(".", "Unmatch."));
			printStreamMatch = new PrintStream(reportFileMatch, "UTF-8");
			printStreamUnMatch = new PrintStream(reportFileUnMatch, "UTF-8");
		}
		matchTypes[0] = reportMatches;
		matchTypes[1] = reportPossibleMatches;
		matchTypes[2] = reportNonMatches;
		xmlOutput = (reportFilePath.endsWith(".xml"));
		emptyValues = writeEmptyValues;
		this.readProperties(new File(configurationFilePath));

		logger.debug("FileMatchListener object constructed successfully");
	}

	@Override
	public void batchReady(int size) {
	}

	@Override
	public void batchDone() {
	}

	/**
	 * Report matches if the corresponding indicator is set.
	 */
	@Override
	public synchronized void matches(Record r1, Record r2, double confidence) {

		counts[0] = counts[0] + 1;
		if (matchTypes[0]) {
			if (xmlOutput)
				printStreamMatch.print("\n\t<Match confidence=\"" + confidence
						+ "\">");
			else
				printStreamMatch.print("\n" + delimiter + "Match" + delimiter
						+ separator + delimiter + confidence + delimiter+separator);
			// Apparently, the records are in the reverse order compared to the
			// groups in the configuration file
			reportRecord(r2, 1, printStreamMatch);
			reportRecord(r1, 2, printStreamMatch);
			if (xmlOutput)
				printStreamMatch.print("\n\t</Match>");
		}

	}

	/**
	 * Report possible matches if the corresponding indicator is set.
	 */
	@Override
	public synchronized void matchesPerhaps(Record r1, Record r2,
			double confidence) {

		counts[1] = counts[1] + 1;
		if (matchTypes[1]) {
			if (xmlOutput)
				printStreamMatch.print("\n\t<MaybeMatch confidence=\""
						+ confidence + "\">");
			else
				printStreamMatch.print("\n" + delimiter + "MaybeMatch"
						+ delimiter + separator + delimiter + confidence
						+ delimiter+separator);
			reportRecord(r2, 1, printStreamMatch);
			reportRecord(r1, 2, printStreamMatch);
			if (xmlOutput)
				printStreamMatch.print("\n\t</MaybeMatch>");
		}
	}

	/**
	 * Report non matches if the corresponding indicator is set.
	 */
	@Override
	public synchronized void noMatchFor(Record record) {

		counts[2] = counts[2] + 1;
		if (matchTypes[2]) {
			// Try to find out if we have a record of type 1 or type 2
			boolean typeOne = true;
			record.getProperties().removeAll(propertyNames.get(1));
			for (String property : record.getProperties()) {
				if (!propertyNames.get(1).contains(property)) {
					typeOne = false;
					break;
				}
			}
			if (xmlOutput)
				printStreamUnMatch.print("\n\t<NonMatch>");
			else
				printStreamUnMatch.print("\n");
			reportRecord(record, (typeOne ? 1 : 2), printStreamUnMatch);
			if (xmlOutput)
				printStreamUnMatch.print("\n\t</NonMatch>");
		}
	}

	@Override
	public void startProcessing() {
		logger.debug("Starting to process the matching events");
		if (xmlOutput) {
			printStreamMatch
					.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			printStreamMatch.print("<MatchingResults>");
			printStreamUnMatch
					.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			printStreamUnMatch.print("<UnMatchingResults>");
		} else {
			printStreamMatch.print(delimiter + "TYPE_MATCH" + delimiter
					+ separator + delimiter + "CONFIDENCE" + delimiter);
			for (Integer index : propertyNames.keySet()) {
				for (String propertyName : propertyNames.get(index)) {
					printStreamMatch.print(separator + delimiter + propertyName
							+ delimiter);
					printStreamUnMatch.print(delimiter + propertyName
							+ delimiter + separator);
				}
			}
		}
	}

	@Override
	public void endProcessing() {

		logger.debug("End of the processing of the matching events");
		if (xmlOutput) {
			printStreamMatch.print("\n</MatchingResults>");
			printStreamUnMatch.print("\n</UnMatchingResults>");
		}
		// Print counters and close reporting file if it's not the console
		String message = "Matches: " + counts[0] + "\tMaybe matches: "
				+ counts[1] + "\tNon matches: " + counts[2];
		logger.info(message);
		System.out.println(message);
		if (reportFileMatch != null)
			printStreamMatch.close();
		if (reportFileUnMatch != null)
			printStreamUnMatch.close();
	}

	/**
	 * Reports a record in the report file.
	 * 
	 * @param record
	 *            The record to report.
	 * @param propertiesIndex
	 *            The index of the property list corresponding to the record.
	 */
	private void reportRecord(Record record, Integer propertiesIndex,
			PrintStream printStream) {

		if (xmlOutput)
			printStream.print("\n\t\t<Record number=\"" + propertiesIndex
					+ "\">");

		for (String propertyName : propertyNames.get(propertiesIndex)) {
			String globalValue = "";
			for (String value : record.getValues(propertyName))
				globalValue += valueSeparator + value;
			// When not empty, the list of values contains no empty or null
			// Strings
			if (globalValue.length() == 0) {
				if (emptyValues) {
					if (xmlOutput)
						printStream.print("\n\t\t\t<" + propertyName + "/>");
					else
						printStream.print(delimiter + " " +delimiter + separator);
				}
			} else {
				if (xmlOutput)
					printStream.print("\n\t\t\t<" + propertyName + ">"
							+ globalValue.substring(1) + "</" + propertyName
							+ ">");
				else
					printStream.print(delimiter + globalValue.substring(1)
							+ delimiter + separator);
			}
		}
		if (xmlOutput)
			printStream.print("\n\t\t</Record>");

	}

	/**
	 * Reads the properties in the Duke configuration file.
	 * 
	 * @param configurationFile
	 *            The configuration file associated to the match operation.
	 * @throws Exception
	 *             When the configuration file cannot be processed or in case of
	 *             XML problem.
	 */
	private void readProperties(File configurationFile) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(configurationFile);

		propertyNames = new HashMap<Integer, List<String>>();
		NodeList groupTags = document.getElementsByTagName("group");
		for (int index = 0; index < groupTags.getLength(); index++) {
			propertyNames.put(index + 1, new ArrayList<String>());
			Element groupTag = (Element) groupTags.item(index);
			NodeList columns = groupTag.getElementsByTagName("column");
			for (int columnIndex = 0; columnIndex < columns.getLength(); columnIndex++) {
				propertyNames.get(index + 1).add(
						((Element) columns.item(columnIndex)).getAttributes()
								.getNamedItem("property").getNodeValue());
			}
		}
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getValueSeparator() {
		return valueSeparator;
	}

	public void setValueSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
	}
}
