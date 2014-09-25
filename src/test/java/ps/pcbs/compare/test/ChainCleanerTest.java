package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import no.priv.garshol.duke.Cleaner;

import org.junit.BeforeClass;
import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.ChainCleaner;

public class ChainCleanerTest {

	static Cleaner cleaner = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		List<String> cleanerNames = Arrays.asList("ps.pcbs.compare.duke.cleaners.LeadingAlphaCleaner", "ps.pcbs.compare.duke.cleaners.TrailingAlphaCleaner");
		cleaner = new ChainCleaner(cleanerNames);

	}

	@Test
	public void testClean() {
		assertEquals(cleaner.clean(" abc 123 xyz "), "123");
	}

}
