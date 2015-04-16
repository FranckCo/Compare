package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.ActivityCodeToDescriptionCleaner;

public class ActivityCodeToDescriptionCleanerTest {

	static ActivityCodeToDescriptionCleaner cleaner = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		cleaner = new ActivityCodeToDescriptionCleaner();
	}

	@Test
	public void testClean() {
		assertEquals("بيع الورق بالجملة", cleaner.clean("46696"));
		
		assertEquals("خضراوات بقولية",cleaner.clean("01133"));
		
		assertEquals("انشطة دعم الاعمال الاخرى للتعدين واستغلال المحاجر",cleaner.clean("09900"));
		
		assertEquals("انشطة المنظمات والاتحادات المهنية الطبية",cleaner.clean("94121"));
		
		   

	}

}
