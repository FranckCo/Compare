package ps.pcbs.compare.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class PCBSIndexer {

	public static void main(String[] args) throws IOException {

		File indexDir = new File("src/main/resources/private/pcbs-index");
		final String CENSUS_FILE_NAME = "src/main/resources/private/new-census-name.txt";

		long start = new Date().getTime();

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, new ArabicAnalyzer(Version.LUCENE_40));
		config.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(FSDirectory.open(indexDir), config);

		Reader reader = new FileReader(CENSUS_FILE_NAME);
		CSVParser parser = new CSVParser(reader, CSVFormat.TDF.withHeader());

		// Loop on the records of the CSV file
		for (CSVRecord record : parser) {
			Document document = new Document();
			Field idField = new StringField("id", record.get(0),  Field.Store.YES); // Indexed, not tokenized, stored
			Field nameField = new TextField("name", record.get(1), Field.Store.YES); // Indexed, tokenized, stored
			document.add(idField);
			document.add(nameField);
			writer.addDocument(document);
		}
		System.out.println(parser.getCurrentLineNumber() + " lines indexed");
		parser.close();
		writer.close();
		long end = new Date().getTime();
		System.out.println("Indexing took " + (end - start) + " milliseconds");
	}
}
