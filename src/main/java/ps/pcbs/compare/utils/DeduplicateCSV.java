package ps.pcbs.compare.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;


import org.apache.log4j.Logger;

import ps.pcbs.compare.Config;

public class DeduplicateCSV {
	
	public static void main(String[] argv) {
		
		DeduplicateCSV dedup=new DeduplicateCSV();
		dedup.stripDuplicatesFromFile(Config.DEDUPLICATES,Config.DUPLICATES);
		logger.info("Début du traitement d'un fichier à dédoublonner");
		
	}
	
	/**
	 * Logger Log4J.
	 */
	private static final Logger logger = Logger.getLogger(DeduplicateCSV.class);
	
	void stripDuplicatesFromFile(String input,String output) {
		Set<String> lines = new LinkedHashSet<String>();
		
		String line;
		String header = null;
		int nbLinesRead=1;
		try (BufferedReader reader = new BufferedReader(
				new FileReader(input))) {
			if(reader.readLine() != null) header=reader.readLine();

			while ((line = reader.readLine()) != null) {
				lines.add(line);
				nbLinesRead++;
				
			}
		} catch (IOException e) {e.printStackTrace();
		}
		int nbLinesKeeped=lines.size();
		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(output))) {
			writer.write(header);
			for (String unique : lines) {
				writer.write(unique);
				writer.newLine();
			}
		} catch (IOException e) {e.printStackTrace();
		}
		
		logger.info(nbLinesRead-nbLinesKeeped+"lignes supprimées pour cause de doublons");
	}

}
