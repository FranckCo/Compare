package ps.pcbs.compare.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ps.pcbs.compare.duke.comparators.MultiComparator;

public class MultiComparatorTest {

	public MultiComparator comparator = new MultiComparator();

	@Test
	public void testClean() {
		
		assertEquals(1, comparator.compare("02345","02345"),0.001);
		assertEquals(0, comparator.compare("0234","02345"),0.001);
		assertEquals(1, comparator.compare("0599288232","#0599288232"),0.001);

		assertEquals(1, comparator.compare("02345#0123456789", "0123456789"),0.001);

	}
}
