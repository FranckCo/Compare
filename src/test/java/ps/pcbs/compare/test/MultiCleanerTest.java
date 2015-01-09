package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ps.pcbs.compare.Config;
import ps.pcbs.compare.duke.cleaners.MultiCleaner;
import ps.pcbs.compare.duke.cleaners.PhoneCleaner;


public class MultiCleanerTest {
	MultiCleaner cleaner = new MultiCleaner();

	

	@Test
	public void testClean() {
		cleaner.setBaseCleaner("ps.pcbs.compare.duke.cleaners.PhoneCleaner");
		cleaner.setSeparator(Config.DEFAULT_TOKEN_SEPARATOR);
		// Spec1
		assertEquals("0512345678#0522345678", cleaner.clean("05*12345678#522345678"));
		assertEquals("#0599288232", cleaner.clean("#0599288232"));
		
		System.out.println(cleaner.clean("02240761#022407610"));

		
		
		
	}
}
