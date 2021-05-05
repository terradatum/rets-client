package org.realtors.rets.util;

import java.util.HashMap;
import java.util.Map;

import org.realtors.rets.util.CaseInsensitiveTreeMap;

import junit.framework.TestCase;

public class CaseInsensitiveTreeMapTest extends TestCase {

	private CaseInsensitiveTreeMap<String, String> map;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.map = new CaseInsensitiveTreeMap<>();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.map = null;
	}

	public void testGetPut() {
		this.map.put("A", "X");
		assertEquals("X", this.map.get("A"));
		assertEquals("X", this.map.get("a"));

		this.map.put("a", "Y");
		assertEquals("Y", this.map.get("a"));
		assertEquals("Y", this.map.get("A"));

		assertEquals(1, this.map.size());
	}

	public void testContainsKey() {
		this.map.put("A", "X");
		assertTrue(this.map.containsKey("A"));
		assertTrue(this.map.containsKey("a"));
	}

	public void testClone() {
		Map<String, String> otherMap = new HashMap<>();
		otherMap.put("A", "X");
		otherMap.put("a", "Y");
		assertEquals(2, otherMap.size());

		CaseInsensitiveTreeMap<String, String> newCitm = new CaseInsensitiveTreeMap<>(otherMap);
		assertEquals(1, newCitm.size());
		// no guarantee of *which* value we'll get, just that they'll be equal
		assertEquals(this.map.get("a"), this.map.get("A"));
	}
}
