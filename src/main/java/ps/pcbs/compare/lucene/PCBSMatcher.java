package ps.pcbs.compare.lucene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import no.priv.garshol.duke.Cleaner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import ps.pcbs.compare.duke.cleaners.RamallahNameCleaner;

public class PCBSMatcher {

	public static void main(String[] args) throws IOException {

		List<String> treated = new ArrayList<String>();

		File indexDir = new File("src/main/resources/private/pcbs-index");
		final String RAMALLAH_FILE_NAME = "src/main/resources/private/new-ramallah-name.txt";

		IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDir));
		IndexSearcher searcher = new IndexSearcher(reader);

		Cleaner cleaner = new RamallahNameCleaner();

		Reader fileReader = new FileReader(RAMALLAH_FILE_NAME);
		CSVParser parser = new CSVParser(fileReader, CSVFormat.TDF.withHeader());

		PrintWriter results = new PrintWriter(new BufferedWriter(new FileWriter("src/main/resources/private/results.txt")));
		 
		Analyzer analyzer =  new ArabicAnalyzer(Version.LUCENE_40);
		QueryParser queryParser = new QueryParser(Version.LUCENE_40, "name", analyzer);

		for (CSVRecord record : parser) {
			if (!record.isConsistent()) continue;
			String id = record.get(0);
			String name = cleaner.clean(record.get(1));
			if (name == null) continue;
			// We select only the first occurrence of a given id
			if (treated.contains(id)) continue;
			treated.add(id);

			// Search the index, limited to 3 results
			Query query;
			try {
				query = queryParser.parse(name);
			} catch (ParseException e) {
				// Continue for now
				continue;
			}
			ScoreDoc[] hits = searcher.search(query, 3).scoreDocs;
			for (int hitIndex = 0; hitIndex < hits.length; hitIndex++) {
				Document document = searcher.doc(hits[hitIndex].doc);
				results.print(hits[hitIndex].score + "\t" + id + "\t" + name + "\t");
				results.println(document.get("id")+ "\t" + document.get("name") + "\n");
			}
		}
		reader.close();
		parser.close();
		results.close();
	}
}
