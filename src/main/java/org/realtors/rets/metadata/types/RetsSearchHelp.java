package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsSearchHelp extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String SEARCHHELPID = "SearchHelpID";
	public static final String VALUE = "Value";

	public RetsSearchHelp() {
		this(DEFAULT_PARSING);
	}

	public RetsSearchHelp(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getSearchHelpID() {
		return getStringAttribute(SEARCHHELPID);
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
		return SEARCHHELPID;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(SEARCHHELPID, sAlphanum32);
		attributeMap.put(VALUE, sText1024);
	}

}
