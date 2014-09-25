package ps.pcbs.compare.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import ps.pcbs.compare.Configuration;

public class Deduplicator {

	public static void main(String[] args) throws IOException {

		Deduplicator deduplicator = new Deduplicator("src/test/resources/coc.txt");

		List<Integer> columns = new ArrayList<Integer>();
		// OrganizationName in Chambers of Commerce file
		columns.add(1);
		
		deduplicator.countDuplicates(columns, true);
	}

	private String file = null;

	/**
	 * Logger Log4J.
	 */
	protected static final Logger logger = Logger.getLogger(Deduplicator.class);

	private Deduplicator(String filePath) {

		if ((filePath == null) || (filePath.length() == 0)) throw new IllegalArgumentException("File path must be provided");
		file = filePath;
	}

	void countDuplicates(List<Integer> columns, boolean reportAll) throws IOException {

		Map<String, Integer> occurrences = new HashMap<String, Integer>();
		MessageDigest digester = DigestUtils.getSha1Digest();
		BufferedReader reader = new BufferedReader(new FileReader(new File(file)));

		String lineIn = reader.readLine(); // First line is column names
		String[] headers = lineIn.split("\t");

		// In a first path, we just calculate the hashes corresponding to possible duplicates
		while ((lineIn = reader.readLine()) != null) {
			String[] components = lineIn.split("\t");
			for (Integer column : columns) {
				digester.update(components[column].getBytes());
			}
			String hash = Hex.encodeHexString(digester.digest());
			if (occurrences.containsKey(hash)) {
				occurrences.put(hash, occurrences.get(hash) + 1);
			} else {
				occurrences.put(hash, 1);
			}
		}
		try {
			reader.close(); // Maybe better to use reset()
		} catch (Exception ignored) {}

		// Reopen input file to get the actual values
		Map<String, Multiplicate> multiplicates = new HashMap<String, Multiplicate>();		
		reader = new BufferedReader(new FileReader(new File(file)));
		lineIn = reader.readLine(); // First line is still column names
		int lineNumber = 0;
		while ((lineIn = reader.readLine()) != null) {
			lineNumber++;
			String[] components = lineIn.split("\t");
			for (Integer column : columns) {
				digester.update(components[column].getBytes());
			}
			String hash = Hex.encodeHexString(digester.digest());
			if (occurrences.containsKey(hash) && occurrences.get(hash) > 1) { // First test is normally always true
				if (multiplicates.containsKey(hash)) multiplicates.get(hash).add(lineNumber, components); // Possible to filter here on specific columns
				else {
					Multiplicate multiplicate = new Multiplicate();
					multiplicate.add(lineNumber, components);
					multiplicates.put(hash, multiplicate);
				}
			}
		}

		// Report
		PrintStream writer = new PrintStream(new File(Configuration.DUPLICATES), "UTF-8");
		for (String hash : multiplicates.keySet()) {
			writer.println("Possible duplicate found");
			Multiplicate multiplicate = multiplicates.get(hash);
			for (Map<Integer, String[]> members : multiplicate.getMembers()) {
				for (int line : members.keySet()) {
					writer.print("Line " + line);
					String[] recordsValue = members.get(line);
					if (reportAll) {
						for (int index = 0; index < recordsValue.length; index++) {
							writer.print("\t" + headers[index] + ": " + recordsValue[index]);
						}
					} else {
						for (int column : columns) {
							writer.print("\t" + headers[column] + ": " + recordsValue[column]);
						}
					}
					writer.println();				
				}
			}
		}
		writer.close();
	}

	private class Multiplicate {

		List<Map<Integer, String[]>>  members = null;

		Multiplicate() {
			members = new ArrayList<Map<Integer, String[]>>();
		}

		void add(int lineNumber, String[] values) {
			Map<Integer, String[]> newMember = new HashMap<Integer, String[]>();
			newMember.put(lineNumber, values);	
		}

		public List<Map<Integer, String[]>> getMembers() {
			return members;
		}
		
	}
}
