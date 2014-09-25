package ps.pcbs.compare;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	// Source files
	//public static String CENSUS = "D:\\En Cours\\PCBS\\Files-3\\Establishment census 2012\\establishment census data.xlsx";
	public static String CENSUS = null;
	public static String RAMALLAH = null;
	public static String AL_BIREH = null;
	public static String CHAMBERS = null;
	public static String COMPANIES = null;
	public static String FINANCE = null;
	public static String ISIC = null;

	// The path to sources which are outside the project are loaded from a .properties file
	static {
		InputStream inputStream = Configuration.class.getResourceAsStream("/sources.properties");
		Properties sources = new Properties();
		try {
			sources.load(inputStream);
			CENSUS = sources.getProperty("ps.pcbs.compare.sources.census");
			RAMALLAH = sources.getProperty("ps.pcbs.compare.sources.ramallah");
			AL_BIREH = sources.getProperty("ps.pcbs.compare.sources.albireh");
			CHAMBERS = sources.getProperty("ps.pcbs.compare.sources.chambers");
			COMPANIES = sources.getProperty("ps.pcbs.compare.sources.companies");
			FINANCE = sources.getProperty("ps.pcbs.compare.sources.finance");
			ISIC = sources.getProperty("ps.pcbs.compare.sources.isic");
		} catch (IOException ignored) {
			// If the properties file can't be found or read, exceptions will be raised when trying to use the sources
		}
	}


	// Miscellaneous files
	public static String PLACES_RAW = "src/main/resources/places-raw.txt";
	public static String PLACES = "src/main/resources/places.txt";
	public static String DUPLICATES = "src/main/resources/duplicates.txt";


	/* Mobile phones structure :

	0599 XX XXXX 
	0598 XX XXXX 
	0597 XX XXXX 
	0595 XX XXXX
	0592 XX XXXX 
	0569 XX XXXX 
	0568 XX XXXX  
	0562 XX XXXX 

	Land phones :

	02 29X XXXX

	Phone prefix for Palestine is +970 or +972.
	*/
	public static String CENSUS_PHONE_REGEX = "-|05(22|62|68|69|92|95|97|98|99)[0-9]{6}|022(4|9)[0-9]{5}";
	public static String RAMALLAH_PHONE_REGEX = "05(22|62|68|69|92|95|97|98|99)[0-9]{6}|022(4|9)[0-9]{5}";

	public static String DEFAULT_SEPARATOR = "\t";
	public static String DEFAULT_DELIMITER = "\"";
}
