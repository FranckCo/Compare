package ps.pcbs.compare.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ps.pcbs.compare.Config;
import ps.pcbs.compare.duke.cleaners.PhoneCleaner;

public class PhoneCleanerTest {

	PhoneCleaner cleaner = new PhoneCleaner();

	@Test
	public void testClean() {

		// Spec1
		assertEquals("0512345678", cleaner.clean("05*12345678"));
		assertEquals("0522345678", cleaner.clean("522345678"));
		// Spec 2
		assertEquals("022312345", cleaner.clean("2312345"));
		assertEquals("022767891", cleaner.clean("2767891"));
		assertEquals("022923456", cleaner.clean("2923456"));
		assertEquals("042412345", cleaner.clean("2412345"));
		assertEquals("092612345", cleaner.clean("2612345"));
		assertEquals("082812345", cleaner.clean("2812345"));
		assertEquals("022959357", cleaner.clean("2959357"));
		// spec3
		assertEquals("022922345", cleaner.clean("2922345."));
		assertEquals("022922345", cleaner.clean("2922345/"));
		assertEquals("022922345", cleaner.clean("2922345-"));

		// spec4
		assertEquals("022722345#022722346", cleaner.clean("2722345/6"));
//		assertEquals("02467891#042467890", cleaner.clean("2467891/0"));
		assertEquals("024678910", cleaner.clean("2467891/0"));

		// spec5
		assertEquals("022922345#022922346#022922347", cleaner.clean("2922345-6-7"));

		// spec6
		assertEquals("022212345#022412346", cleaner.clean("2212345-2412346"));
		assertEquals("022512345#022412346", cleaner.clean("2512345+2412346"));

		// spec7
		assertEquals("022912345#0591234567", cleaner.clean("2912345-0591234567"));

		// spec8
		assertEquals("022912345#022967891", cleaner.clean("2912345-2967891"));

		// spec9
		assertEquals("022912345#022967891", cleaner.clean("-2912345  2967891"));

		// spec10
		assertEquals("0599123456", cleaner.clean("1599123456"));
		assertEquals("0544123456", cleaner.clean("1544123456"));

		// spec11
		assertEquals("0597111400", cleaner.clean("0597111400محمد"));
		//spec12
//		assertEquals("0229845432", cleaner.clean("2ع2984543"));
		
		//spec13
		assertEquals("022987441", cleaner.clean("2987441/المحل"));
		
		//spec14
		assertEquals("0599316494", cleaner.clean("0599316494عبد لناصر"));
		
		//spec15
		assertEquals("0591234567", cleaner.clean("0591234567سامر"));
		
		//spec17
		assertEquals("022987441",cleaner.clean("2987441/المحل"));
		
		//spec18
		assertEquals("022912345",cleaner.clean("02-2912345"));
		
		//spec19
		assertEquals("0599291234",cleaner.clean("0599-291234"));
		
		//spec19
		assertEquals("0599123456",cleaner.clean("05990599123456"));
		
		//spec20
		assertEquals("0599521218",cleaner.clean("0599521218/نديم"));
		
		//spec20
		assertEquals("022912345",cleaner.clean("2912345/البيت"));
		
		//spec21
		assertEquals("0599316494",cleaner.clean("0599316494عبد لناصر"));
		
		//spec22
		assertEquals("0591234567", cleaner.clean("0591234567سامر"));
		
		assertEquals("022952220#022954193", cleaner.clean("2952220+2954193"));
		
		// new cases from Hebron 
		assertEquals("0599379511#0599379511", cleaner.clean("0599379511 0599379511"));
		assertEquals("0599262852#0599373860", cleaner.clean("0599262852 0599373860"));
		
		assertEquals("022228158#0599241575", cleaner.clean("2228158 0599241575"));
		assertEquals("022258965#0599337470", cleaner.clean("2258965 0599337470"));
		
		assertEquals("022225940", cleaner.clean("2225940"));
		
		assertEquals("022227593", cleaner.clean("02/2227593"));
				
		assertEquals("022214060#0599939387", cleaner.clean("02/2214060 0599939387"));
		assertEquals("022217766#0599278980", cleaner.clean("02/2217766 0599278980"));
		
		assertEquals("0599278980", cleaner.clean("0599278980"));
		
		assertEquals(cleaner.clean("022980408"), cleaner.clean("2988459"));
		
//		assertEquals("05969442011", cleaner.clean("969442011"));
		
		
		
//		String s="0599521218يم /نديم د256";
//		String s1="0599521218/";
//		s=s.replaceAll("\\p{InArabic}+","");
//		System.out.println(s);
//		s=s.replace("/",Config.DEFAULT_TOKEN_SEPARATOR);
//		s1=s1.replace("/",Config.DEFAULT_TOKEN_SEPARATOR);
//		System.out.println(s);
//		System.out.println(s1);
//		String s3="12345-5-6-8-9";
//		s3=s3.replaceAll("\\s+", " ")
//		.replace("/", Config.DEFAULT_TOKEN_SEPARATOR)
//		.replace("+", Config.DEFAULT_TOKEN_SEPARATOR)
//		.replace("-", Config.DEFAULT_TOKEN_SEPARATOR)
//		.replace("|", Config.DEFAULT_TOKEN_SEPARATOR);
//		System.out.println(s3);
		
	}

}
