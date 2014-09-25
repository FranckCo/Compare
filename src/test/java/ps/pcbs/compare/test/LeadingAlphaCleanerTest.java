package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.LeadingAlphaCleaner;

public class LeadingAlphaCleanerTest {

	static LeadingAlphaCleaner cleaner = new LeadingAlphaCleaner();

	@Test
	public void testClean() {
		assertNull(cleaner.clean(null));
		assertEquals(cleaner.clean("").length(), 0);
		assertEquals(cleaner.clean("abc123xyz"), "123xyz");
		assertEquals(cleaner.clean(" abc 123 xyz "), "123 xyz ");
		assertEquals("23", cleaner.clean("Mange des pommes 23"));
		assertEquals("11", cleaner.clean("حوض11"));

	}

}
