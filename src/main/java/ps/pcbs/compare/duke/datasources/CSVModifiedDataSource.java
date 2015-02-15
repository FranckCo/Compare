package ps.pcbs.compare.duke.datasources;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.io.Reader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;

import ps.pcbs.compare.Config;
import no.priv.garshol.duke.Record;
import no.priv.garshol.duke.DukeException;
import no.priv.garshol.duke.RecordIterator;
import no.priv.garshol.duke.DukeConfigException;
import no.priv.garshol.duke.datasources.*;
import no.priv.garshol.duke.utils.CSVReader;


/**
 * @author Karreem
 * 
 * Modified CSVDataSource from Duke in order to be able to compare n-tuple of columns
 * The operator used to concatenate variables is "#" separator
 * 
 *
 */
public class CSVModifiedDataSource extends ColumnarDataSource {
	private String file;
	private String encoding;
	private Reader directreader; // overrides 'file'; used for testing
	private int skiplines;
	private boolean hasheader;
	private char separator;

	public CSVModifiedDataSource() {
		super();
		this.hasheader = true;
	}

	public String getInputFile() {
		return file;
	}

	public void setInputFile(String file) {
		this.file = file;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getSkipLines() {
		return skiplines;
	}

	public void setSkipLines(int skiplines) {
		this.skiplines = skiplines;
	}

	public boolean getHeaderLine() {
		return hasheader;
	}

	public void setHeaderLine(boolean hasheader) {
		this.hasheader = hasheader;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	// this is used only for testing
	public void setReader(Reader reader) {
		this.directreader = reader;
	}

	public RecordIterator getRecords() {
		if (directreader == null)
			verifyProperty(file, "input-file");

		try {
			Reader in;
			if (directreader != null)
				in = directreader;
			else {
				if (encoding == null)
					in = new FileReader(file);
				else
					in = new InputStreamReader(new FileInputStream(file),
							encoding);
			}

			CSVReader csv = new CSVReader(in);
			if (separator != 0)
				csv.setSeparator(separator);
			return new CSVRecordIterator(csv);
		} catch (FileNotFoundException e) {
			throw new DukeConfigException("Couldn't find CSV file '" + file
					+ "'");
		} catch (IOException e) {
			throw new DukeException(e);
		}
	}

	protected String getSourceName() {
		return "CSV";
	}

	public class CSVRecordIterator extends RecordIterator {
		private CSVReader reader;
		/**
		 * modified from int to Map of Integer in order to be able to deal
		 * with concatenation of colums. The operator to add columns is #
		 */
		private Map<Integer, List<Integer>> index; // what index in row to find colum[ix]
										
		private Column[] column; // all the columns, in random order
		private RecordBuilder builder;
		private Record nextrecord;

		public CSVRecordIterator(CSVReader reader) throws IOException {
			this.reader = reader;
			this.builder = new RecordBuilder(CSVModifiedDataSource.this);

			// index here is random 0-n. index[0] gives the column no in the CSV
			// file, while colname[0] gives the corresponding column name.

			index = new HashMap<>();
			column = new Column[columns.size()];
			


			// skip the required number of lines before getting to the data
			for (int ix = 0; ix < skiplines; ix++)
				reader.next();

			// learn column indexes from header line (if there is one)
			String[] header = null;
			if (hasheader)
				header = reader.next();
			else {
				// find highest column number
				int high = 0;
				for (Column c : getColumns())
					high = Math.max(high, Integer.parseInt(c.getName()));

				// build corresponding index
				header = new String[high];
				for (int ix = 0; ix < high; ix++)
					header[ix] = "" + (ix + 1);
			}

			// build the 'index' and 'column' indexes
			int count = 0;
			
			

			int nb;
			for (Column c : getColumns()) {

				List<String> names = null;
				// we split the column names according to the "#" separator
				if (c.getName().contains(Config.DEFAULT_TOKEN_SEPARATOR)) {
// local variable used to determine if we deal with a variable which is obtained by concatenating 2 variables 

					names = new ArrayList<String>(Arrays.asList(c.getName()
							.split(Config.DEFAULT_TOKEN_SEPARATOR)));

				}
				// local variable used to determine if we success to find the variable in the csv file 
				boolean found = false;
				nb=0;
				// local variable used to determine which variable of the csv file use to fulfill the column
				List<Integer> i=new ArrayList<Integer>();
				for (int ix = 0; ix < header.length; ix++) {
					
					if ((names == null) || names.isEmpty()) {
						if (header[ix].equals(c.getName())) {
							i.add(ix);
							index.put(count,i);
							column[count++] = c;
							found = true;
							break;
						}
					} else {
						// case of a variable which is obtained by concatenating 2 variables
						for (String colname : names) {

							if (header[ix].equals(colname)) {
								i.add(ix);
								nb++;

							}
						}
						// once we have found variables in the csv file we save the indexes in the map
						if (nb == names.size()) {
							found = true;
							index.put(count,i);
							column[count++] = c;
							break;
						}
					}
				}
				if (!found)
					throw new DukeConfigException("Column " + c.getName()
							+ " not found " + "in CSV file");
			}
			

			findNextRecord();
		}

		private void findNextRecord() {
			String[] row;
			try {
				row = reader.next();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (row == null) {
				nextrecord = null; // there isn't any next record
				return;
			}

			// build a record from the current row
			builder.newRecord();



				
			String rowValue;
				for (Entry<Integer, List<Integer>> entry : index.entrySet()) {

					int j=0;
					StringBuilder rows = new StringBuilder();

					
					for (int i:entry.getValue()){
						if (i >= row.length)
							continue; // order is arbitrary, so we might not be done yet
						rowValue = row[i]
								.replaceAll("\\s+", " ").trim();
						rows.append(rowValue);
						if(entry.getValue().size()>1 && j<entry.getValue().size()-1){
							rows.append(Config.DEFAULT_TOKEN_SEPARATOR);
							j++;
						}
					}
					builder.addValue(column[entry.getKey()], rows.toString());
				}
				
			
			nextrecord = builder.getRecord();
		}

		public boolean hasNext() {
			return (nextrecord != null);
		}

		public Record next() {
			Record thenext = nextrecord;
			findNextRecord();
			return thenext;
		}
	}
}