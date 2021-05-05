package org.realtors.rets.metadata.attrib;

import org.realtors.rets.metadata.AttrType;

public class AttrTextTest extends AttrTypeTest {
	public void testAttrText() throws Exception {
		AttrType<String> parser = new AttrText(0, 10);
		String[] good = { "\r\n\t", "eabc\rdefg", };
		String[] bad = { (char) 7 + "", (char) 1 + "", "12345678901", };

		assertEquals(parser.getType(), String.class);

		for (String s : good) {
			assertEquals(s, parser.parse(s, true));
		}

		for (String s : bad) {
			assertParseException(parser, s);
		}
	}
}
