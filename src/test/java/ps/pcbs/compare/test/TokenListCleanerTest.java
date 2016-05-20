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
		
		//Step1
		cleaner.setToken("رام ,مكتب,و شركاه,مغلق ابو علي,مغلق سفيان,مغلق سلطان,مغلق,غير موجود ابو علي,غير موجود سفيان,غير موجود مكانه,لا يوجد ابو علي,لا يوجد سفيان,الشركة,الشركه,شركة,دكتور,شركه,شراكة,شراكه,واخوانه,وإخوانه,واولاده,وأولاده,محلات,محل,م.خ.م,م. خ.م,م.خ,م.ع.ع,م.ع.م,م ع م,م ع"); 


		
		assertEquals(cleaner.clean("برستيج للاستثمار السياحي باسل"),cleaner.clean("شركة برستيج للاستثمار السياحي باسل"));

		
		
		assertEquals(cleaner.clean("الإعتماد"), cleaner.clean("شراكة الإعتماد شركه شركة شراكه"));
		
		
		assertEquals(cleaner.clean("الإعتماد"), cleaner.clean("الإعتماد  محل"));
	
	
		assertEquals(cleaner.clean("الإعتماد"), cleaner.clean("الإعتماد وإخوانه واولاده وأولاده محلات محل"));

		assertEquals("", cleaner.clean("دكتور"));
		
		
		System.out.println(cleaner.clean("شركة الامانة "));
		

		



		
		
		
//		cleaner = new TokenListCleaner(Arrays.asList("Chapi", "Chapo"));
//		assertEquals("Patapo,   Patapi", cleaner.clean("Chapi Chapo Patapo, Chapo Chapi Patapi"));
	}
}
