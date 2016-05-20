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
		System.out.println(cleaner.clean("and"));
		System.out.println(cleaner.clean(" \"مخمر موز\" "));
		System.out.println(cleaner.clean("الامانة شركة"));
		
		System.out.println(cleaner.clean("الامانةghj  شركة"));
		System.out.println(cleaner.clean("تكسي   المأمون "));
		
		System.out.println(cleaner.clean("كيك  & بيك"));
		System.out.println(cleaner.clean("art &  glft"));
		System.out.println(cleaner.clean(""));
		System.out.println(cleaner.clean("المركز الفني للصيانه"));
		
		System.out.println(cleaner.clean("كافتيريا الامانه"));
		
		System.out.println(cleaner.clean("تكسي المأمون "));
		
		System.out.println(cleaner.clean("شركه الإعتماد"));
		
		System.out.println(cleaner.clean("يور هوم 4"));
		
		
		System.out.println(cleaner.clean("4 me"));
		
		//Step2
		System.out.println(cleaner.clean("ابو محمد"));
		
		System.out.println(cleaner.clean("أبو    أحمد"));

		System.out.println(cleaner.clean("عبد   الكريم"));
		
		System.out.println(cleaner.clean("engendré par un aléa donné"));
		System.out.println(cleaner.clean("العمري فاروق"));
		System.out.println(cleaner.clean("ياسين العمري"));
		System.out.println(cleaner.clean("ياسين   العمري"));
		System.out.println(cleaner.clean("ياسين العمري "));
		System.out.println(cleaner.clean("  ياسين العمري"));
		System.out.println(cleaner.clean(" ياسين   العمري "));
		
		System.out.println(cleaner.clean("مؤنسة الشامي عبد الرؤوف و شيماء بغدادي"));
		System.out.println(cleaner.clean("كفر عين"));
		System.out.println(cleaner.clean("عين يبرود"));
		System.out.println(cleaner.clean("نيو ستايل"));
		System.out.println(cleaner.clean("بيت لقيا"));
		
		
		
		System.out.println(cleaner.clean("BLUE DRY CLEAN"));
		
		
		//Step5
		System.out.println(cleaner.clean("blue dry clean"));

	}

}
