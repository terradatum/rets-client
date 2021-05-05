package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsValidationLookupType extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDTEXT = "ValidText";
	public static final String PARENT1VALUE = "Parent1Value";
	public static final String PARENT2VALUE = "Parent2Value";

	public RetsValidationLookupType() {
		this(DEFAULT_PARSING);
	}

	public RetsValidationLookupType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidText() {
		return getStringAttribute(VALIDTEXT);
	}

	public String getParent1Value() {
		return getStringAttribute(PARENT1VALUE);
	}

	public String getParent2Value() {
		return getStringAttribute(PARENT2VALUE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return null;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(VALIDTEXT, sAlphanum32);
		attributeMap.put(PARENT1VALUE, sAlphanum32);
		attributeMap.put(PARENT2VALUE, sAlphanum32);
	}

}
