package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import no.priv.garshol.duke.Cleaner;

import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.TokenListCleaner;

public class TokenListCleanerTest {

	@Test
	public void testClean() {

		TokenListCleaner cleaner = new TokenListCleaner();
		cleaner.setToken("ه,رام الله,مكتب,شركة,دكتور,شركه,ة");
		System.out.print(cleaner.getToken());
		assertEquals("برستيج للاستثمار السياحي باسل",cleaner.clean("شركة برستيج للاستثمار السياحي باسل"));
		
		assertEquals(cleaner.clean("المركز الفني للصيانه"),cleaner.clean("المركز الفي للصيانة"));

		assertEquals("13", cleaner.clean("13دكتور"));
//		cleaner = new TokenListCleaner(Arrays.asList("Chapi", "Chapo"));
//		assertEquals("Patapo,   Patapi", cleaner.clean("Chapi Chapo Patapo, Chapo Chapi Patapi"));
	}
}
