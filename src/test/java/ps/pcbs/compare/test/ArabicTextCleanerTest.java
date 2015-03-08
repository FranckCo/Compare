package ps.pcbs.compare.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.ArabicTextCleaner;

public class ArabicTextCleanerTest {

	static ArabicTextCleaner cleaner = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		cleaner = new ArabicTextCleaner();
	}

	@Test
	public void testClean() {
		assertEquals(cleaner.clean("كافتيريا الامانة"), "كافتيريا الامانه");
		assertEquals(cleaner.clean("تكسي المأمون"), "تكسي المامون");
		assertEquals(cleaner.clean("تكسي الإعتماد"), "تكسي الاعتماد");
		assertEquals(cleaner.clean("تكسي الإعتماد"), "تكسي الاعتماد");
	}

}
