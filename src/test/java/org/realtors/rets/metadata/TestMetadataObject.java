package org.realtors.rets.metadata;

import java.util.Map;

class TestMetadataObject extends MetadataObject {
	
	public static final String SYSTEM_NAME = "SystemName";
	public static final String STRING1 = "String1";
	private static int sAddAttributeCount;

	public TestMetadataObject(boolean strictParsing) {
		super(strictParsing);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return SYSTEM_NAME;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(SYSTEM_NAME, sAlphanum10);
		attributeMap.put(STRING1, sText);
		sAddAttributeCount++;
	}

	public String getSystemName() {
		return getStringAttribute(SYSTEM_NAME);
	}

	public String getString1() {
		return getStringAttribute(STRING1);
	}

	public static void resetAddAttributeCount() {
		sAddAttributeCount = 0;
	}

	public static int getAddAttributeCount() {
		return sAddAttributeCount;
	}

}
