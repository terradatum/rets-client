package org.realtors.rets.metadata.types;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataObject;
import org.realtors.rets.metadata.MetadataType;

import java.util.Collection;
import java.util.Map;

public class RetsClass extends MetadataObject {
	public static final String CLASSNAME = "ClassName";
	public static final String VISIBLENAME = "VisibleName";
	public static final String STANDARDNAME = "StandardName";
	public static final String DESCRIPTION = "Description";
	public static final String TABLEVERSION = "TableVersion";
	public static final String TABLEDATE = "TableDate";
	public static final String UPDATEVERSION = "UpdateVersion";
	public static final String UPDATEDATE = "UpdateDate";
	public static final String DELETEDFLAGFIELD = "DeletedFlagField";
	public static final String DELETEDFLAGVALUE = "DeletedFlagValue";
	public static final String CLASSTIMESTAMP = "ClassTimeStamp";
	public static final String HASHKEYINDEX = "HasKeyIndex";
	private static final MetadataType[] sTypes = {MetadataType.UPDATE, MetadataType.TABLE};

	public RetsClass() {
		this(DEFAULT_PARSING);
	}

	public RetsClass(boolean strictParsing) {
		super(strictParsing);
	}

	@Override
	public MetadataType[] getChildTypes() {
		return sTypes;
	}

	public String getClassName() {
		return getStringAttribute(CLASSNAME);
	}

	public String getVisibleName() {
		return getStringAttribute(VISIBLENAME);
	}

	public String getStandardName() {
		return getStringAttribute(STANDARDNAME);
	}

	public String getDescription() {
		return getStringAttribute(DESCRIPTION);
	}

	public int getTableVersion() {
		return getIntAttribute(TABLEVERSION);
	}

	public String getTableDate() {
		return getDateAttribute(TABLEDATE);
	}

	public int getUpdateVersion() {
		return getIntAttribute(UPDATEVERSION);
	}

	public String getUpdateDate() {
		return getDateAttribute(UPDATEDATE);
	}

	public RetsUpdate getRetsUpdate(String updateName) {
		return (RetsUpdate) getChild(MetadataType.UPDATE, updateName);
	}

	public RetsUpdate[] getRetsUpdates() {
		return getChildren(MetadataType.UPDATE).toArray(new RetsUpdate[0]);
	}

	public RetsTable getRetsTable(String systemName) {
		return (RetsTable) getChild(MetadataType.TABLE, systemName);
	}

	public RetsTable[] getRetsTables() {
		Collection<RetsTable> children = getChildren(MetadataType.TABLE);
		return children.toArray(new RetsTable[0]);
	}

	@Override
	protected String getIdAttr() {
		return CLASSNAME;
	}

	@Override
	protected void addAttributesToMap(Map<String, AttrType<?>> attributeMap) {
		attributeMap.put(CLASSNAME, sAlphanum32);
		attributeMap.put(VISIBLENAME, sPlaintext32);
		attributeMap.put(STANDARDNAME, sAlphanum32);
		attributeMap.put(DESCRIPTION, sPlaintext128);
		attributeMap.put(TABLEVERSION, sAttrVersion);
		attributeMap.put(TABLEDATE, sAttrDate);
		attributeMap.put(UPDATEVERSION, sAttrVersion);
		attributeMap.put(UPDATEDATE, sAttrDate);
		attributeMap.put(DELETEDFLAGFIELD, retsname);
		attributeMap.put(DELETEDFLAGVALUE, sAlphanum32);
		attributeMap.put(CLASSTIMESTAMP, retsname);
		attributeMap.put(HASHKEYINDEX, sAttrBoolean);
	}


	public String getDeletedFlagField() {
		return getStringAttribute(DELETEDFLAGFIELD);
	}

	public String getDeletedFlagValue() {
		return getStringAttribute(DELETEDFLAGVALUE);
	}

	public String getClassTimeStamp() {
		return getStringAttribute(CLASSTIMESTAMP);
	}

	public String getHashKeyIndex() {
		return getStringAttribute(HASHKEYINDEX);
	}

}
