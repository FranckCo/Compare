package ps.pcbs.compare.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.TokenListCleaner;
import ps.pcbs.compare.duke.comparators.WordCountComparator;

public class WordCountComparatorTest {

	WordCountComparator comparator = new WordCountComparator(4);
	TokenListCleaner cleaner = new TokenListCleaner();
	

	@Test
	public void testClean() {
		cleaner.setToken("ه,رام الله,مكتب,شركة,دكتور,شركه,ة");

		// Spec1
		// assertEquals("0512345678", cleaner.clean("05*12345678"));
		// assertEquals("0522345678", cleaner.clean("522345678"));

		String v1 = "محددة صناديق للشاحنات";
		String v2 = "محددة صناديق";
		String v3 = "شركة مايكروتيك للاستيراد والتسويق";
		String v4 = "مايكروتيك للاستيراد والتسويق";

		assertEquals(0.0, comparator.compare(v1, v2), 0.001);
		assertEquals(0.0, comparator.compare(v3, v4), 0.001);

		String v5 = "اي ام اي";
		String v6 = "صالون في اي بي ام";
		assertEquals(0.0, comparator.compare(v5, v6), 0.001);
		
		String v7="شركة المهدي للاستيراد والتجارة العامة";
		v7=cleaner.clean(v7);
		String v8="شركة الفيصل للاستيراد والتصدير والتجارة العامة";
		v8=cleaner.clean(v8);
		assertEquals(0.0, comparator.compare(v7, v8), 0.001);
		
		String v9="شركة بيرفكست للتجارة والخدمات العامة";
		v9=cleaner.clean(v9);
		String v10="شركة ايجل للتجارة والخدمات العامة";
		v10=cleaner.clean(v10);
		assertEquals(0.0, comparator.compare(v9, v10), 0.001);
		
	}

}
