package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsLookupType extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String LONGVALUE = "LongValue";
	public static final String SHORTVALUE = "ShortValue";
	public static final String VALUE = "Value";

	public RetsLookupType() {
		this(DEFAULT_PARSING);
	}

	public RetsLookupType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getLongValue() {
		return getStringAttribute(LONGVALUE);
	}

	public String getShortValue() {
		return getStringAttribute(SHORTVALUE);
	}

	public String getValue() {
		return getStringAttribute(VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return VALUE;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(LONGVALUE, sText128);
		attributeMap.put(SHORTVALUE, sText32);
		attributeMap.put(VALUE, sAlphanum32);
	}

}
