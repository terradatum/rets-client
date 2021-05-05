package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;
import org.realtors.rets.metadata.attrib.AttrEnum;

import java.util.Map;

public class RetsValidationExpression extends MetadataObject {

	public static final String METADATAENTRYID = "MetadataEntryID";
	public static final String VALIDATIONEXPRESSIONID = "ValidationExpressionID";
	public static final String VALIDATIONEXPRESSIONTYPE = "ValidationExpressionType";
	public static final String VALUE = "Value";
	private static final String[] VALIDATIONEXPRESSIONTYPES = "ACCEPT,REJECT,SET".split(",");
	private static final AttrType<String> sExpressionType = new AttrEnum(VALIDATIONEXPRESSIONTYPES);

	public RetsValidationExpression() {
		this(DEFAULT_PARSING);
	}

	public RetsValidationExpression(boolean strictParsing) {
		super(strictParsing);
	}

	public String getMetadataEntryID() {
		return getStringAttribute(METADATAENTRYID);
	}

	public String getValidationExpressionID() {
		return getStringAttribute(VALIDATIONEXPRESSIONID);
	}

	public String getValidationExpressionType() {
		return getStringAttribute(VALIDATIONEXPRESSIONTYPE);
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
		return VALIDATIONEXPRESSIONID;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(METADATAENTRYID, sAttrMetadataEntryId);
		attributeMap.put(VALIDATIONEXPRESSIONID, sAlphanum32);
		attributeMap.put(VALIDATIONEXPRESSIONTYPE, sExpressionType);
		attributeMap.put(VALUE, sText512);
	}

}
