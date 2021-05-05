package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;
import org.realtors.rets.metadata.attrib.AttrGenericText;

import java.util.Map;

public class RetsUpdateType extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SYSTEMNAME = "SystemName";
	public static final String SEQUENCE = "Sequence";
	public static final String ATTRIBUTES = "Attributes";
	public static final String DEFAULT = "Default";
	public static final String VALIDATIONEXPRESSIONID = "ValidationExpressionID";
	public static final String UPDATEHELPID = "UpdateHelpID";
	public static final String VALIDATIONLOOKUPNAME = "ValidationLookupName";
	public static final String VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String MAXCHOICE = "MaxChoice";
	public static final String MAXUPDATE = "MaxUpdate";

	private static final AttrType<String> sAttributes = new AttrGenericText(0, 10, "12345,");

	public RetsUpdateType() {
		this(DEFAULT_PARSING);

	}

	public RetsUpdateType(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getSystemName() {
		return getStringAttribute(SYSTEMNAME);
	}

	public int getSequence() {
		return getIntAttribute(SEQUENCE);
	}

	public String getAttributes() {
		return getStringAttribute(ATTRIBUTES);
	}

	public String getDefault() {
		return getStringAttribute(DEFAULT);
	}

	public String getValidationExpressionID() {
		return getStringAttribute(VALIDATIONEXPRESSIONID);
	}

	public String getUpdateHelpID() {
		return getStringAttribute(UPDATEHELPID);
	}

	public String getValidationLookupName() {
		return getStringAttribute(VALIDATIONLOOKUPNAME);
	}

	public String getValidationExternalName() {
		return getStringAttribute(VALIDATIONEXTERNALNAME);
	}

	public int getMaxChoice() {
		return getIntAttribute(MAXCHOICE);
	}

	public int getMaxUpdate() {
		return getIntAttribute(MAXUPDATE);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sNoChildren;
	}

	@Override
	protected String getIdAttr() {
		return SYSTEMNAME;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(SYSTEMNAME, sAlphanum32);
		attributeMap.put(SEQUENCE, sAttrNumeric);
		attributeMap.put(ATTRIBUTES, sAttributes);
		attributeMap.put(DEFAULT, sPlaintext);
		attributeMap.put(VALIDATIONEXPRESSIONID, sAlphanum32);
		attributeMap.put(UPDATEHELPID, sAlphanum32);
		attributeMap.put(VALIDATIONLOOKUPNAME, sAlphanum32);
		attributeMap.put(VALIDATIONEXTERNALNAME, sAlphanum32);
		attributeMap.put(MAXCHOICE, sAttrNumeric);
		attributeMap.put(MAXUPDATE, sAttrNumeric);
	}

}
