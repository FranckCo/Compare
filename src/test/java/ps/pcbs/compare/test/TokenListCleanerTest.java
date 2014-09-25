package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import no.priv.garshol.duke.Cleaner;

import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.TokenListCleaner;

public class TokenListCleanerTest {

	@Test
	public void testClean() {

		Cleaner cleaner = new TokenListCleaner(Arrays.asList("bali", "balo"));
		assertEquals(cleaner.clean(" bali toto balo "), "toto");

		cleaner = new TokenListCleaner(Arrays.asList("حي", "حوض", "حى"));
		assertEquals("13", cleaner.clean("حوض 13"));

		cleaner = new TokenListCleaner(Arrays.asList("Chapi", "Chapo"));
		assertEquals("Patapo,   Patapi", cleaner.clean("Chapi Chapo Patapo, Chapo Chapi Patapi"));
	}
}
