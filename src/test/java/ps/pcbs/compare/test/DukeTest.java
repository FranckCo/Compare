package ps.pcbs.compare.test;

import java.util.ArrayList;
import java.util.List;

import no.priv.garshol.duke.Cleaner;
import no.priv.garshol.duke.ConfigurationImpl;
import no.priv.garshol.duke.Property;
import no.priv.garshol.duke.PropertyImpl;
import no.priv.garshol.duke.comparators.ExactComparator;
import no.priv.garshol.duke.datasources.CSVDataSource;
import no.priv.garshol.duke.datasources.Column;

import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.PhoneCleaner;

public class DukeTest {

	@Test
	public void testConfiguration() {

	    ExactComparator comparator = new ExactComparator();
	    List<Property> properties = new ArrayList<Property>();
	    properties.add(new PropertyImpl("ID"));
	    Property name = new PropertyImpl("NAME", comparator, 0.3, 0.8);
	    properties.add(name);
	    Property email = new PropertyImpl("EMAIL", comparator, 0.3, 0.8);
	    properties.add(email);
	    Property phone = new PropertyImpl("PHONE", comparator, 0.4, 0.51);
	    properties.add(phone);
	    phone.setLookupBehaviour(Property.Lookup.REQUIRED);

	    ConfigurationImpl configuration = new ConfigurationImpl();
	    configuration.setThreshold(0.85);
	    configuration.setProperties(properties);
	    configuration.isDeduplicationMode();
	    configuration.validate();
	}
	
	@Test
	public void testDataSource() {

		CSVDataSource source = new CSVDataSource();
		source.setSeparator('\t');
		source.setEncoding("UTF8");
		source.setInputFile("src/test/resources/adam.txt");
		Column firstColumn = new Column("reference", null, "", null);

	}

	@Test
	public void testColumn() {

		Cleaner phoneCleaner = new PhoneCleaner();
		Column testColumn = new Column("Phone", "Phone", "", phoneCleaner);

	}
}
