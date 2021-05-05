package org.realtors.rets.metadata.types;

//import java.util.Date;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsUpdate extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String UPDATENAME = "UpdateName";
	public static final String DESCRIPTION = "Description";
	public static final String KEYFIELD = "KeyField";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";
	public static final String UPDATETYPEVERSION = "UpdateTypeVersion";
	public static final String UPDATETYPEDATE = "UpdateTypeDate";
	private static final MetadataType[] sTypes = {MetadataType.UPDATE_TYPE};

	public RetsUpdate() {
		this(DEFAULT_PARSING);
	}

	public RetsUpdate(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getUpdateName() {
		return getStringAttribute(UPDATENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public int getVersion() {
		int v = getIntAttribute(VERSION);
		if (v == 0) {
			v = getIntAttribute(UPDATETYPEVERSION);
		}
		return v;
	}

	public String getDate() {
		String d = getDateAttribute(DATE);
		if (d == null) {
			d = getDateAttribute(UPDATETYPEDATE);
		}
		return d;
	}

	public RetsUpdateType getRetsUpdateType(String systemName) {
		return (RetsUpdateType) getChild(MetadataType.UPDATE_TYPE, systemName);
	}

	public RetsUpdateType[] getRetsUpdateTypes() {
		RetsUpdateType[] tmpl = new RetsUpdateType[0];
		return getChildren(MetadataType.UPDATE_TYPE).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sTypes;
	}

	@Override
	protected String getIdAttr() {
		return UPDATENAME;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(UPDATENAME, sAlphanum24);
		attributeMap.put(DESCRIPTION, sPlaintext64);
		attributeMap.put(KEYFIELD, sAlphanum32);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(DATE, sAttrDate);
		attributeMap.put(UPDATETYPEVERSION, sAttrVersion);
		attributeMap.put(UPDATETYPEDATE, sAttrDate);
	}
}
