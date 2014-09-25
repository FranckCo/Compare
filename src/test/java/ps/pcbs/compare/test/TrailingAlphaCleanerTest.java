package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.TrailingAlphaCleaner;

public class TrailingAlphaCleanerTest {

	static TrailingAlphaCleaner cleaner = new TrailingAlphaCleaner();

	@Test
	public void testClean() {

		assertNull(cleaner.clean(null));
		assertEquals(cleaner.clean("").length(), 0);
		assertEquals(cleaner.clean("abc123xyz"), "abc123");
		assertEquals(cleaner.clean(" abc 123 xyz "), " abc 123");
		assertEquals("23", cleaner.clean("23 Mange des pommes"));
		assertEquals("11", cleaner.clean("11حوض"));
		assertEquals("A", cleaner.clean("A"));	}
}
