package org.realtors.rets.client;

import junit.framework.TestCase;

import java.io.InputStream;

public abstract class RetsTestCase extends TestCase {
	protected static InputStream getResource(String name) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResourceAsStream(name);
	}

	public void assertEquals(String message, Object[] expected, Object[] actual) {
		boolean success;
		if (expected.length == actual.length) {
			success = true;
			for (int i = 0; i < expected.length; i++) {
				if (!expected[i].equals(actual[i])) {
					success = false;
					break;
				}
			}
		} else {
			success = false;
		}
		if (!success) {
			fail(message + " expected: " + arrayToString(expected) + " but got: " + arrayToString((actual)));
		}
	}

	private String arrayToString(Object[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < array.length; i++) {
			Object o = array[i];
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("\"");
			sb.append(o.toString());
			sb.append("\"");
		}
		sb.append("}");
		return sb.toString();
	}
}
