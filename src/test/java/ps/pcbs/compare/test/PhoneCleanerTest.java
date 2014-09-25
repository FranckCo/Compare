package ps.pcbs.compare.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ps.pcbs.compare.duke.cleaners.PhoneCleaner;

public class PhoneCleanerTest {

	PhoneCleaner cleaner = new PhoneCleaner();

	@Test
	public void testClean() {
		assertEquals("022412345", cleaner.clean("2412345"));
		assertEquals("022967367", cleaner.clean("2967367"));
		assertEquals("022412345", cleaner.clean("02412345"));
		assertEquals("022967367", cleaner.clean("02967367"));
		assertEquals("022412345", cleaner.clean("22412345"));
		assertEquals("022967367", cleaner.clean("22967367"));

		assertEquals("0522654321", cleaner.clean("522654321"));
		assertEquals("0562654321", cleaner.clean("562654321"));
		assertEquals("0568654321", cleaner.clean("568654321"));
		assertEquals("0569654321", cleaner.clean("569654321"));
		assertEquals("0592654321", cleaner.clean("592654321"));
		assertEquals("0595654321", cleaner.clean("595654321"));
		assertEquals("0597654321", cleaner.clean("597654321"));
	}

}
