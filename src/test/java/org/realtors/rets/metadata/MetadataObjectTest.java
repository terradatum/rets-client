package org.realtors.rets.metadata;

public class MetadataObjectTest extends MetadataTestCase {
	public void testStrictAttributes() {
		MetadataObject.clearAttributeMapCache();
		TestMetadataObject metaObject = createTestMetaObject(MetadataObject.STRICT_PARSING);
		assertEquals("SomeName", metaObject.getSystemName());
		assertEquals("Foo Bar", metaObject.getString1());
		assertEquals("somename", metaObject.getAttributeAsString("systemname"));
		assertEquals("foo bar", metaObject.getAttributeAsString("string1"));
	}

	public void testLooseAttributes() {
		MetadataObject.clearAttributeMapCache();
		TestMetadataObject metaObject = createTestMetaObject(MetadataObject.LOOSE_PARSING);
		assertEquals("somename", metaObject.getSystemName());
		assertEquals("foo bar", metaObject.getString1());
	}

	public void testCache() {
		TestMetadataObject.resetAddAttributeCount();
		MetadataObject.clearAttributeMapCache();
		createTestMetaObject(MetadataObject.STRICT_PARSING);
		createTestMetaObject(MetadataObject.LOOSE_PARSING);
		createTestMetaObject(MetadataObject.STRICT_PARSING);
		createTestMetaObject(MetadataObject.LOOSE_PARSING);
		assertEquals(2, TestMetadataObject.getAddAttributeCount());
		MetadataObject.clearAttributeMapCache();
		createTestMetaObject(MetadataObject.STRICT_PARSING);
		createTestMetaObject(MetadataObject.LOOSE_PARSING);
		createTestMetaObject(MetadataObject.STRICT_PARSING);
		createTestMetaObject(MetadataObject.LOOSE_PARSING);
		assertEquals(4, TestMetadataObject.getAddAttributeCount());
	}

	private TestMetadataObject createTestMetaObject(boolean strictParsing) {
		TestMetadataObject metaObject = new TestMetadataObject(strictParsing);
		metaObject.setAttribute("SystemName", "SomeName");
		metaObject.setAttribute("systemname", "somename");
		metaObject.setAttribute("String1", "Foo Bar");
		metaObject.setAttribute("string1", "foo bar");
		return metaObject;
	}
}
