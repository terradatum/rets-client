package org.realtors.rets.metadata.types;

//import java.util.Date;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsSystem extends MetadataObject {
	public static final String SYSTEMID = "SystemID";
	public static final String SYSTEMDESCRIPTION = "SystemDescription";
	public static final String COMMENTS = "Comments";
	public static final String DATE = "Date";
	public static final String VERSION = "Version";
	public static final String TIMEZONEOFFSET = "TimeZoneOffset";
	public static final MetadataType[] CHILDREN = {MetadataType.RESOURCE, MetadataType.FOREIGNKEYS};

	public RetsSystem() {
		this(DEFAULT_PARSING);
	}

	public RetsSystem(boolean strictParsing) {
		super(strictParsing);
	}

	public String getSystemID() {
		return getStringAttribute(SYSTEMID);
	}

	public String getComment() {
		return getStringAttribute(COMMENTS);
	}

	public String getSystemDescription() {
		return getStringAttribute(SYSTEMDESCRIPTION);
	}

	public String getDate() {
		return getDateAttribute(DATE);
	}

	public String getTimeZoneOffset() {
		return getDateAttribute(TIMEZONEOFFSET);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public RetsResource getRetsResource(String resourceID) {
		return (RetsResource) getChild(MetadataType.RESOURCE, resourceID);
	}

	public RetsResource[] getRetsResources() {
		RetsResource[] tmpl = new RetsResource[0];
		return getChildren(MetadataType.RESOURCE).toArray(tmpl);
	}

	public RetsForeignKey getRetsForeignKey(String foreignKeyID) {
		return (RetsForeignKey) getChild(MetadataType.FOREIGNKEYS, foreignKeyID);
	}

	public RetsForeignKey[] getRetsForeignKeys() {
		RetsForeignKey[] tmpl = new RetsForeignKey[0];
		return getChildren(MetadataType.FOREIGNKEYS).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return null;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(SYSTEMID, sAlphanum10);
		attributeMap.put(SYSTEMDESCRIPTION, sPlaintext64);
		attributeMap.put(DATE, sAttrDate);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(COMMENTS, sText);
		attributeMap.put(TIMEZONEOFFSET, sAttrDate);
	}

}
