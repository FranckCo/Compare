package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ps.pcbs.compare.Config;
import ps.pcbs.compare.duke.comparators.CommercialOwnerNameComparator;

public class CommercialOwnerNameComparatorTest {

	@Test
	public void testCompare() {

		CommercialOwnerNameComparator comparator = new CommercialOwnerNameComparator();

		String v1 = null;
		String v2 = null;

		assertEquals(1.0, comparator.compare(v1, v2), 0.001);

		v1 = "#";
		v2 = "#";

		assertEquals(1.0, comparator.compare(v1, v2), 0.001);

		v1 = "االريحان للخضار والفواكه#";
		v2 = "االريحان للخضار والفواكه#";

		assertEquals(1.0, comparator.compare(v1, v2), 0.001);

		v1 = "الريحان للخضار والفواكه#كريم";
		v2 = "الريحان للخضار والفواكه#رشد";
		assertEquals(0.0, comparator.compare(v1, v2), 0.001);
		
		v1="#كريم إبن عمر";
		v2="#كريم إبن عمر";
		assertEquals(1.0, comparator.compare(v1, v2), 0.001);
		
		v1="صلون كريم#كريم إبن عمر";
		v2="مطعم كريم#كريم إبن عمر";
		// need to improve the spec because it is there a false positive
		assertEquals(1.0, comparator.compare(v1, v2), 0.001);
		
		v1="مطعم في وسط المدينة#كريم تشفين";
		v2="مطعم في وسط المدينة#عبد الكريم الجطب";
		
		assertEquals(0.0, comparator.compare(v1, v2), 0.001);
		
		v1="مطعم في وسط المدينة#كريم تشفين";
		v2="مطعم في وسط المدينة#عبد  كريم الجطب";
		
		assertEquals(1.0, comparator.compare(v1, v2), 0.001);

		
		v1="بال بوكس#فراس سليم عبد ابو حمد وحماد جمال ابراهيم علاونه";
		v2="المحامي جمال حمد#جمال ابراهيم حمد";
		
		assertEquals(0.0, comparator.compare(v1, v2), 0.001);
		
		
		
	

	}

}