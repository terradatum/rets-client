package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsUpdateHelp extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String UPDATEHELPID = "UpdateHelpID";
	public static final String VALUE = "Value";

	public RetsUpdateHelp() {
		this(DEFAULT_PARSING);
	}

	public RetsUpdateHelp(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getUpdateHelpID() {
		return getStringAttribute(UPDATEHELPID);
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
		return UPDATEHELPID;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(UPDATEHELPID, sAlphanum32);
		attributeMap.put(VALUE, sText1024);
	}

}
