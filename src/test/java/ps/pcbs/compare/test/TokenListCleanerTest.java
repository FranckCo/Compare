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
		cleaner.setToken("رام ه,مكتب,شركة,دكتور,شركه,شراكة,شراكه,الشركة,الشركه,واخوانه,وإخوانه,واولاده,وأولاده,محلات,محل,ة,ه"); 


		
		assertEquals(cleaner.clean("and"),cleaner.clean("     _ *   + =  ) ( & ^ % $ # @ ! ~  : ;  ?> < } { ’ , . [ ] )"));
		assertEquals(cleaner.clean("برستيج للاستثمار السياحي باسل"),cleaner.clean("شركة برستيج للاستثمار السياحي باسل"));
		assertEquals(cleaner.clean(" \"مخمر موز\" "),cleaner.clean("مخمر موز"));
		assertEquals(cleaner.clean("كيك  & بيك"), cleaner.clean("كيك  و بيك"));
		assertEquals(cleaner.clean("art &  glft"), cleaner.clean("art and  glft"));
		assertEquals(cleaner.clean(""),cleaner.clean(""));
		assertEquals(cleaner.clean("المركز الفني للصيانه"),cleaner.clean("المركز الفني للصيانة"));
		
		assertEquals(cleaner.clean("كافتيريا الامانه"), cleaner.clean("كافتيريا الامانة"));
		
		assertEquals(cleaner.clean("تكسي المأمون "), cleaner.clean("تكسي المامون  "));
		
		assertEquals(cleaner.clean("شركه الإعتماد"), cleaner.clean("شركه الاعتماد"));
		
		assertEquals(cleaner.clean("الإعتماد"), cleaner.clean("شراكة الإعتماد شركه شركة شراكه"));
		
		
		assertEquals(cleaner.clean("الإعتماد"), cleaner.clean("الإعتماد  محل"));
	
	
		assertEquals(cleaner.clean("الإعتماد"), cleaner.clean("الإعتماد وإخوانه واولاده وأولاده محلات محل"));

		assertEquals("", cleaner.clean("13دكتور"));
		

		
		assertEquals(cleaner.clean("يور هوم 4"), cleaner.clean("فور يور هوم"));
		
		
		assertEquals(cleaner.clean("4 me"), cleaner.clean("for me"));
		
		//Step2
		assertEquals(cleaner.clean("ابو محمد"), cleaner.clean("ابومحمد"));
		
		assertEquals(cleaner.clean("أبو أحمد"), cleaner.clean("أبوأحمد"));
		
		assertEquals(cleaner.clean("عبد الكريم"), cleaner.clean("عبدالكريم"));
		
		assertEquals(cleaner.clean("دير ابوضعيف"), cleaner.clean("ديرابوضعيف"));
		assertEquals(cleaner.clean("بير زيت"), cleaner.clean("بيرزيت"));
		assertEquals(cleaner.clean("كفر عين"), cleaner.clean("كفر عين"));
		assertEquals(cleaner.clean("عين يبرود"), cleaner.clean("عينيبرود"));
		assertEquals(cleaner.clean("نيو ستايل"), cleaner.clean("نيوستايل"));
		assertEquals(cleaner.clean("بيت لقيا"), cleaner.clean("بيتلقيا"));
		
		
		
		assertEquals(cleaner.clean("BLUE DRY CLEAN"), cleaner.clean("blue dry clean"));
		
		
		//Step5
		System.out.println(cleaner.clean("blue dry clean"));


		
		
		
//		cleaner = new TokenListCleaner(Arrays.asList("Chapi", "Chapo"));
//		assertEquals("Patapo,   Patapi", cleaner.clean("Chapi Chapo Patapo, Chapo Chapi Patapi"));
	}
}
