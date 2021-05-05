package org.realtors.rets.metadata.types;

//import java.util.Date;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsLookup extends MetadataObject {
	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String LOOKUPNAME = "LookupName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";
	public static final String LOOKUPTYPEVERSION = "LookupTypeVersion";
	public static final String LOOKUPTYPEDATE = "LookupTypeDate";
	private static final MetadataType[] CHILDREN = {MetadataType.LOOKUP_TYPE};
	private static final RetsLookupType[] EMPTYLOOKUPTYPES = {};

	public RetsLookup() {
		this(DEFAULT_PARSING);
	}

	public RetsLookup(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getLookupName() {
		return getStringAttribute(LOOKUPNAME);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public int getVersion() {

		int ver = getIntAttribute(VERSION);
		if (ver == 0) {
			ver = getIntAttribute(LOOKUPTYPEVERSION);
		}
		return ver;
	}

	public String getDate() {
		String date = getDateAttribute(DATE);
		if (date == null) {
			date = getDateAttribute(LOOKUPTYPEDATE);
		}
		return date;
	}

	public RetsLookupType getRetsLookupType(String value) {
		return (RetsLookupType) getChild(MetadataType.LOOKUP_TYPE, value);
	}

	public RetsLookupType[] getRetsLookupTypes() {
		return getChildren(MetadataType.LOOKUP_TYPE).toArray(EMPTYLOOKUPTYPES);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return LOOKUPNAME;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(LOOKUPNAME, sAlphanum32);
		attributeMap.put(VISIBLENAME, sPlaintext32);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(DATE, sAttrDate);
		attributeMap.put(LOOKUPTYPEVERSION, sAttrVersion);
		attributeMap.put(LOOKUPTYPEDATE, sAttrDate);
	}

}
