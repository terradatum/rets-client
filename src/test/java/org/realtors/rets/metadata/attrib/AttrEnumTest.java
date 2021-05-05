package org.realtors.rets.metadata.attrib;

import org.realtors.rets.metadata.AttrType;

public class AttrEnumTest extends AttrTypeTest {
	public void testEnum() throws Exception {
		String[] values = { "One", "Two", "Three" };
		AttrType<String> parser = new AttrEnum(values);
		for (String value : values) {
			assertEquals(value, parser.render(parser.parse(value, true)));
		}
		assertParseException(parser, "Four");
		assertParseException(parser, "");
		assertParseException(parser, "three");
	}
}
