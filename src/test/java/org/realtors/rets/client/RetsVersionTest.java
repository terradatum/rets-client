package org.realtors.rets.client;

public class RetsVersionTest extends RetsTestCase {
	
	@SuppressWarnings("deprecation")
	public void testEquals() {
		assertEquals("Checking 1.0", RetsVersion.v1_0, new RetsVersion(1, 0));

		assertEquals("Checking 1.5", RetsVersion.v1_5, new RetsVersion(1, 5));
		
		assertEquals("Checking 1.7", RetsVersion.v1_7, new RetsVersion(1, 7));
		
		assertEquals("Checking 1.7.2", RetsVersion.v1_7_2, new RetsVersion(1, 7, 2, 0));
		
		assertEquals("Checking revision support", RetsVersion.v1_7_2, new RetsVersion(1, 7, 2, 0));
		
		assertFalse("Checking draft support", RetsVersion.v1_5.equals(new RetsVersion(1, 5, 0, 1)));

		assertFalse("Checking backwards compatible draft support", RetsVersion.v1_5.equals(new RetsVersion(1, 5, 1)));
	}

	@SuppressWarnings("deprecation")
	public void testToString() {
		assertEquals("Checking toString() 1.0", "RETS/1.0", RetsVersion.v1_0.toString());
		assertEquals("Checking toString() 1.5", "RETS/1.5", RetsVersion.v1_5.toString());
		assertEquals("Checking toString() 1.7", "RETS/1.7", RetsVersion.v1_7.toString());
		assertEquals("Checking toString() 1.7.2", "RETS/1.7.2", RetsVersion.v1_7_2.toString());
		assertEquals("Checking toString() backward compatible draft without revision", "RETS/1.5d1", new RetsVersion(1, 5, 1).toString());
		assertEquals("Checking toString() revision with draft", "RETS/1.7.2d1", new RetsVersion(1, 7, 2, 1).toString());
	}
}
