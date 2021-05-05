package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsValidationExternalType extends MetadataObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SEARCHFIELD = "SearchField";
	public static final String DISPLAYFIELD = "DisplayField";
	public static final String RESULTFIELDS = "ResultFields";

	public RetsValidationExternalType() {
		this(DEFAULT_PARSING);
	}

	public RetsValidationExternalType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getSearchField() {
		return getStringAttribute(SEARCHFIELD);
	}

	public String getDisplayField() {
		return getStringAttribute(DISPLAYFIELD);
	}

	public String getResultFields() {
		return getStringAttribute(RESULTFIELDS);
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
		attributeMap.put(SEARCHFIELD, sPlaintext512);
		attributeMap.put(DISPLAYFIELD, sPlaintext512);
		attributeMap.put(RESULTFIELDS, sPlaintext1024);
	}

}
