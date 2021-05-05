package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsEditMask extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String EDITMASKID = "EditMaskID";
	public static final String VALUE = "Value";

	public RetsEditMask() {
		this(DEFAULT_PARSING);
	}

	public RetsEditMask(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getEditMaskID() {
		return getStringAttribute(EDITMASKID);
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
		return EDITMASKID;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(EDITMASKID, sAlphanum32);
		attributeMap.put(VALUE, sText256);
	}

}
