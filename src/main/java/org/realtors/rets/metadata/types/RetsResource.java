package org.realtors.rets.metadata.types;

//import java.util.Date;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Map;

public class RetsResource extends MetadataObject {
	public static final String RESOURCEID = "ResourceID";
	public static final String STANDARDNAME = "StandardName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String DESCRIPTION = "Description";
	public static final String KEYFIELD = "KeyField";
	public static final String CLASSCOUNT = "ClassCount";
	public static final String CLASSVERSION = "ClassVersion";
	public static final String CLASSDATE = "ClassDate";
	public static final String OBJECTVERSION = "ObjectVersion";
	public static final String OBJECTDATE = "ObjectDate";
	public static final String SEARCHHELPVERSION = "SearchHelpVersion";
	public static final String SEARCHHELPDATE = "SearchHelpDate";
	public static final String EDITMASKVERSION = "EditMaskVersion";
	public static final String EDITMASKDATE = "EditMaskDate";
	public static final String LOOKUPVERSION = "LookupVersion";
	public static final String LOOKUPDATE = "LookupDate";
	public static final String UPDATEHELPVERSION = "UpdateHelpVersion";
	public static final String UPDATEHELPDATE = "UpdateHelpDate";
	public static final String VALIDATIONEXPRESSIONVERSION = "ValidationExpressionVersion";
	public static final String VALIDATIONEXPRESSIONDATE = "ValidationExpressionDate";
	public static final String VALIDATIONLOOKUPVERSION = "ValidationLookupVersion";
	public static final String VALIDATIONLOOKUPDATE = "ValidationLookupDate";
	public static final String VALIDATIONEXTERNALVERSION = "ValidationExternalVersion";
	public static final String VALIDATIONEXTERNALDATE = "ValidationExternalDate";
	private static final MetadataType[] CHILDREN = {
			MetadataType.VALIDATION_EXPRESSION,
			MetadataType.LOOKUP,
			MetadataType.CLASS,
			MetadataType.OBJECT,
			MetadataType.VALIDATION_EXTERNAL,
			MetadataType.VALIDATION_LOOKUP,
			MetadataType.EDITMASK,
			MetadataType.UPDATE_HELP,
			MetadataType.SEARCH_HELP
	};

	public RetsResource() {
		this(DEFAULT_PARSING);
	}

	public RetsResource(boolean strictParsing) {
		super(strictParsing);
	}

	public String getResourceID() {
		return getStringAttribute(RESOURCEID);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public String getKeyField() {
		return getStringAttribute(KEYFIELD);
	}

	public int getClassCount() {
		return getIntAttribute(CLASSCOUNT);
	}

	public int getClassVersion() {
		return getIntAttribute(CLASSVERSION);
	}

	public String getClassDate() {
		return getDateAttribute(CLASSDATE);
	}

	public int getObjectVersion() {
		return getIntAttribute(OBJECTVERSION);
	}

	public String getObjectDate() {
		return getDateAttribute(OBJECTDATE);
	}

	public int getSearchHelpVersion() {
		return getIntAttribute(SEARCHHELPVERSION);
	}

	public String getSearchHelpDate() {
		return getDateAttribute(SEARCHHELPDATE);
	}

	public int getEditMaskVersion() {
		return getIntAttribute(EDITMASKVERSION);
	}

	public String getEditMaskDate() {
		return getDateAttribute(EDITMASKDATE);
	}

	public int getLookupVersion() {
		return getIntAttribute(LOOKUPVERSION);
	}

	public String getLookupDate() {
		return getDateAttribute(LOOKUPDATE);
	}

	public int getUpdateHelpVersion() {
		return getIntAttribute(UPDATEHELPVERSION);
	}

	public String getUpdateHelpDate() {
		return getDateAttribute(UPDATEHELPDATE);
	}

	public int getValidationExpressionVersion() {
		return getIntAttribute(VALIDATIONEXPRESSIONVERSION);
	}

	public String getValidationExpressionDate() {
		return getDateAttribute(VALIDATIONEXPRESSIONDATE);
	}

	public int getValidationLookupVersion() {
		return getIntAttribute(VALIDATIONLOOKUPVERSION);
	}

	public String getValidationLookupDate() {
		return getDateAttribute(VALIDATIONLOOKUPDATE);
	}

	public int getValidationExternalVersion() {
		return getIntAttribute(VALIDATIONEXTERNALVERSION);
	}

	public String getValidationExternalDate() {
		return getDateAttribute(VALIDATIONEXTERNALDATE);
	}

	public RetsValidationExpression getRetsValidationExpression(String validationExpressionID) {
		return (RetsValidationExpression) getChild(MetadataType.VALIDATION_EXPRESSION, validationExpressionID);
	}

	public RetsValidationExpression[] getRetsValidationExpressions() {
		RetsValidationExpression[] tmpl = new RetsValidationExpression[0];
		return getChildren(MetadataType.VALIDATION_EXPRESSION).toArray(tmpl);
	}

	public RetsLookup getRetsLookup(String lookupName) {
		return (RetsLookup) getChild(MetadataType.LOOKUP, lookupName);
	}

	public RetsLookup[] getRetsLookups() {
		RetsLookup[] tmpl = new RetsLookup[0];
		return getChildren(MetadataType.LOOKUP).toArray(tmpl);
	}

	public RetsClass getRetsClass(String className) {
		return (RetsClass) getChild(MetadataType.CLASS, className);
	}

	public RetsClass[] getRetsClasses() {
		RetsClass[] tmpl = new RetsClass[0];
		return getChildren(MetadataType.CLASS).toArray(tmpl);
	}

	public RetsObject getRetsObject(String objectType) {
		return (RetsObject) getChild(MetadataType.OBJECT, objectType);
	}

	public RetsObject[] getRetsObjects() {
		RetsObject[] tmpl = new RetsObject[0];
		return getChildren(MetadataType.OBJECT).toArray(tmpl);
	}

	public RetsValidationExternal getRetsValidationExternal(String validationExternalName) {
		return (RetsValidationExternal) getChild(MetadataType.VALIDATION_EXTERNAL, validationExternalName);
	}

	public RetsValidationExternal[] getRetsValidationExternal() {
		RetsValidationExternal[] tmpl = new RetsValidationExternal[0];
		return getChildren(MetadataType.VALIDATION_EXTERNAL).toArray(tmpl);
	}

	public RetsValidationLookup getRetsValidationLookup(String validationLookupName) {
		return (RetsValidationLookup) getChild(MetadataType.VALIDATION_LOOKUP, validationLookupName);
	}

	public RetsValidationLookup[] getRetsValidationLookups() {
		RetsValidationLookup[] tmpl = new RetsValidationLookup[0];
		return getChildren(MetadataType.VALIDATION_LOOKUP).toArray(tmpl);
	}

	public RetsEditMask getRetsEditMask(String editMaskID) {
		return (RetsEditMask) getChild(MetadataType.EDITMASK, editMaskID);
	}

	public RetsEditMask[] getRetsEditMasks() {
		RetsEditMask[] tmpl = new RetsEditMask[0];
		return getChildren(MetadataType.EDITMASK).toArray(tmpl);
	}

	public RetsUpdateHelp getRetsUpdateHelp(String updateHelpID) {
		return (RetsUpdateHelp) getChild(MetadataType.UPDATE_HELP, updateHelpID);
	}

	public RetsUpdateHelp[] getRetsUpdateHelps() {
		RetsUpdateHelp[] tmpl = new RetsUpdateHelp[0];
		return getChildren(MetadataType.UPDATE_HELP).toArray(tmpl);
	}

	public RetsSearchHelp getRetsSearchHelp(String searchHelpID) {
		return (RetsSearchHelp) getChild(MetadataType.SEARCH_HELP, searchHelpID);
	}

	public RetsSearchHelp[] getRetsSearchHelps() {
		RetsSearchHelp[] tmpl = new RetsSearchHelp[0];
		return getChildren(MetadataType.SEARCH_HELP).toArray(tmpl);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return CHILDREN;
	}

	@Override
	protected String getIdAttr() {
		return RESOURCEID;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(RESOURCEID, sAlphanum32);
		attributeMap.put(STANDARDNAME, sAlphanum32);
		attributeMap.put(VISIBLENAME, sPlaintext32);
		attributeMap.put(DESCRIPTION, sPlaintext64);
		attributeMap.put(KEYFIELD, sAlphanum32);
		attributeMap.put(CLASSCOUNT, sAttrNumeric);
		attributeMap.put(CLASSVERSION, sAttrVersion);
		attributeMap.put(CLASSDATE, sAttrDate);
		attributeMap.put(OBJECTVERSION, sAttrVersion);
		attributeMap.put(OBJECTDATE, sAttrDate);
		attributeMap.put(SEARCHHELPVERSION, sAttrVersion);
		attributeMap.put(SEARCHHELPDATE, sAttrDate);
		attributeMap.put(EDITMASKVERSION, sAttrVersion);
		attributeMap.put(EDITMASKDATE, sAttrDate);
		attributeMap.put(LOOKUPVERSION, sAttrVersion);
		attributeMap.put(LOOKUPDATE, sAttrDate);
		attributeMap.put(UPDATEHELPVERSION, sAttrVersion);
		attributeMap.put(UPDATEHELPDATE, sAttrDate);
		attributeMap.put(VALIDATIONEXPRESSIONVERSION, sAttrVersion);
		attributeMap.put(VALIDATIONEXPRESSIONDATE, sAttrDate);
		attributeMap.put(VALIDATIONLOOKUPVERSION, sAttrVersion);
		attributeMap.put(VALIDATIONLOOKUPDATE, sAttrDate);
		attributeMap.put(VALIDATIONEXTERNALVERSION, sAttrVersion);
		attributeMap.put(VALIDATIONEXTERNALDATE, sAttrDate);
	}

}
