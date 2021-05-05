package org.realtors.rets.metadata.types;

//import java.util.Date;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsValidationExternal extends MetadataObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONEXTERNALNAME = "ValidationExternalName";
	public static final String SEARCHRESOURCE = "SearchResource";
	public static final String SEARCHCLASS = "SearchClass";
	public static final String VERSION = "Version";
	public static final String DATE = "Date";
	private static final MetadataType[] CHILDREN = {MetadataType.VALIDATION_EXTERNAL_TYPE};

	public RetsValidationExternal() {
		this(DEFAULT_PARSING);
	}

	public RetsValidationExternal(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidationExternalName() {
		return getStringAttribute(VALIDATIONEXTERNALNAME);
	}

	public String getSearchResource() {
		return getStringAttribute(SEARCHRESOURCE);
	}

	public String getSearchClass() {
		return getStringAttribute(SEARCHCLASS);
	}

	public int getVersion() {
		return getIntAttribute(VERSION);
	}

	public String getDate() {
		return getDateAttribute(DATE);
	}

	public RetsValidationExternalType[] getRetsValidationExternalTypes() {
		RetsValidationExternalType[] tmpl = new RetsValidationExternalType[0];
		return getChildren(MetadataType.VALIDATION_EXTERNAL_TYPE).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return VALIDATIONEXTERNALNAME;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(VALIDATIONEXTERNALNAME, sAlphanum32);
		attributeMap.put(SEARCHRESOURCE, sAlphanum32);
		attributeMap.put(SEARCHCLASS, sAlphanum32);
		attributeMap.put(VERSION, sAttrVersion);
		attributeMap.put(DATE, sAttrDate);
	}

}
