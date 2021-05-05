package org.realtors.rets.metadata;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.metadata.attrib.*;
import org.realtors.rets.util.CaseInsensitiveTreeMap;

import java.io.Serializable;
import java.util.*;

public abstract class MetadataObject implements Serializable {
	public static final boolean STRICT_PARSING = true;
	public static final boolean LOOSE_PARSING = false;
	public static final boolean DEFAULT_PARSING = LOOSE_PARSING;
	/**
	 * a standard parser used by different child types
	 */
	protected static final AttrType<String> sAlphanum = new AttrAlphanum(0, 0);
	protected static final AttrType<String> sAlphanum64 = new AttrAlphanum(1, 64);
	protected static final AttrType<String> sAlphanum32 = new AttrAlphanum(1, 32);
	protected static final AttrType<String> sAlphanum24 = new AttrAlphanum(1, 24);
	protected static final AttrType<String> sAlphanum10 = new AttrAlphanum(1, 10);
	protected static final AttrType<String> sPlaintext = new AttrPlaintext(0, 0);
	protected static final AttrType<String> sPlaintext1024 = new AttrPlaintext(1, 1024);
	protected static final AttrType<String> sPlaintext512 = new AttrPlaintext(1, 512);
	protected static final AttrType<String> sPlaintext128 = new AttrPlaintext(1, 128);
	protected static final AttrType<String> sPlaintext64 = new AttrPlaintext(1, 64);
	protected static final AttrType<String> sPlaintext32 = new AttrPlaintext(1, 32);
	protected static final AttrType<String> sText = new AttrText(0, 0);
	protected static final AttrType<String> sText1024 = new AttrText(1, 1024);
	protected static final AttrType<String> sText512 = new AttrText(1, 512);
	protected static final AttrType<String> sText256 = new AttrText(1, 256);
	protected static final AttrType<String> sText128 = new AttrText(1, 128);
	protected static final AttrType<String> sText64 = new AttrText(1, 64);
	protected static final AttrType<String> sText32 = new AttrText(1, 32);
	protected static final AttrType<Boolean> sAttrBoolean = new AttrBoolean();
	protected static final AttrType<String> sAttrDate = new AttrDate();
	protected static final AttrType<Integer> sAttrNumeric = new AttrNumeric();
	protected static final AttrType<Integer> sAttrNumericPositive = new AttrNumericPositive();
	protected static final AttrType<Integer> sAttrVersion = new AttrVersion();
	protected static final AttrType<String> sAttrMetadataEntryId = sAlphanum32;
	protected static final MetadataType[] sNoChildren = new MetadataType[0];
	protected static final AttrType<String> retsid = sAlphanum32;
	protected static final AttrType<String> retsname = sAlphanum64;
	private static final Log LOG = LogFactory.getLog(MetadataObject.class);
	private static final Map<CacheKey, Map<String, AttrType<?>>> sAttributeMapCache = new HashMap<>();
	/**
	 * map of child type to map of child id to child object
	 */
	protected final Map<MetadataType, Map<String, MetadataObject>> childTypes;
	/**
	 * map of attribute name to attribute object (as parsed by attrtype)
	 */
	protected final Map<String, Object> attributes;
	/**
	 * map of attribute name to AttrType parser
	 */
	protected final Map<String, AttrType<?>> attrTypes;
	private final boolean strict;
	/**
	 * the metadata path to this object
	 */
	protected String path;
	private MetadataCollector mCollector;

	public MetadataObject(boolean strictParsing) {
		this.strict = strictParsing;
		if (strictParsing) {
			this.attributes = new HashMap<>();
		} else {
			this.attributes = new CaseInsensitiveTreeMap<>();
		}
		this.attrTypes = this.getAttributeMap(strictParsing);
		MetadataType[] types = getChildTypes();
		this.childTypes = new HashMap<>();
		for (MetadataType type : types) {
			this.childTypes.put(type, null);
		}
	}

	public static void clearAttributeMapCache() {
		synchronized (sAttributeMapCache) {
			sAttributeMapCache.clear();
		}
	}

	private Map<String, AttrType<?>> getAttributeMap(boolean strictParsing) {
		synchronized (sAttributeMapCache) {
			Map<String, AttrType<?>> map = sAttributeMapCache.get(new CacheKey(this, strictParsing));
			if (map == null) {
				if (strictParsing) {
					map = new HashMap<>();
				} else {
					map = new CaseInsensitiveTreeMap<>();
				}
				addAttributesToMap(map);
				// Let's make sure no one mucks with the map later
				map = Collections.unmodifiableMap(map);
				sAttributeMapCache.put(new CacheKey(this, strictParsing), map);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Adding to attribute cache: " + this.getClass().getName() + ", " + strictParsing);
				}
			}
			return map;
		}
	}

	public <T extends MetadataObject> Collection<T> getChildren(MetadataType type) {
		if (!this.childTypes.containsKey(type)) {
			// throw new IllegalArgumentException?
			return null;
		}
		Map<String, T> o = (Map<String, T>) this.childTypes.get(type);
		if (o == null) {
			if (!fetchChildren(type)) {
				return new ArrayList<>();
			}
			o = (Map<String, T>) this.childTypes.get(type);
		}
		return o.values();
	}

	private boolean fetchChildren(MetadataType type) {
		this.childTypes.put(type, new HashMap<>());
		MetadataObject[] children = null;
		if (this.mCollector != null) {
			children = this.mCollector.getMetadata(type, getPath());
		}
		if (children == null) {
			return false;
		}
		for (MetadataObject child : children) {
			addChild(type, child);
		}
		return true;
	}

	public MetadataObject getChild(MetadataType type, String id) {
		if (id == null) {
			return null;
		}
		try {
			if (this.childTypes.get(type) == null && this.mCollector != null) {
				if (!fetchChildren(type)) {
					return null;
				}
			}
			Map<String, MetadataObject> m = this.childTypes.get(type);
			if (m == null) {
				return null;
			}
			return m.get(id);
		} catch (ClassCastException e) {
			return null;
		}
	}

	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	public Set<String> getKnownAttributes() {
		return this.attrTypes.keySet();
	}

	public String getAttributeAsString(String key) {
		Object value = this.attributes.get(key);
		if (value == null) {
			return null;
		}
		if (this.attrTypes.containsKey(key)) {
			AttrType<?> type = this.attrTypes.get(key);
			return type.render(value);
		}
		return value.toString();
	}

	protected Object getTypedAttribute(String key, Class<?> type) {
		AttrType<?> atype = this.attrTypes.get(key);
		if (atype == null) {
			return null;
		}
		if (atype.getType() == type) {
			return this.attributes.get(key);
		}
		LOG.warn("type mismatch, expected " + type.getName() + " but" + " got " + atype.getType().getName());
		return null;
	}

	public String getDateAttribute(String key) {
		return (String) getTypedAttribute(key, String.class);
	}

	public String getStringAttribute(String key) {
		return (String) getTypedAttribute(key, String.class);
	}

	public int getIntAttribute(String key) {
		Integer i = (Integer) getTypedAttribute(key, Integer.class);
		if (i == null) {
			return 0;
		}
		return i;
	}

	public boolean getBooleanAttribute(String key) {
		Boolean b = (Boolean) getTypedAttribute(key, Boolean.class);
		if (b == null) {
			return false;
		}
		return b;
	}

	public void setAttribute(String key, String value) {
		if (value == null) {
			// LOG.warning()
			return;
		}
		if (this.attrTypes.containsKey(key)) {
			AttrType<?> type = this.attrTypes.get(key);
			try {
				this.attributes.put(key, type.parse(value, this.strict));
			} catch (MetadataParseException e) {
				LOG.warn(this + " couldn't parse attribute " + key + ", value " + value + ": " + e.getMessage());
			}
		} else {
			this.attributes.put(key, value);
			LOG.warn("Unknown key (" + this + "): " + key);
		}
	}

	public void addChild(MetadataType type, MetadataObject child) {
		if (this.childTypes.containsKey(type)) {
			Map<String, MetadataObject> obj = this.childTypes.get(type);
			Map<String, MetadataObject> map;
			if (obj == null) {
				map = new HashMap<>();
				this.childTypes.put(type, map);
			} else {
				map = obj;
			}
			if (child == null) {
				return;
			}
			String id = child.getId();

			child.setPath(this.getPath());
			child.setCollector(this.mCollector);
			if (id != null) {
				map.put(id, child);
			}
		}
	}

	public String getId() {
		String idAttr = getIdAttr();
		if (idAttr == null) {
			/* cheap hack so everything's a damn map */
			return Integer.toString(hashCode());
		}
		return getAttributeAsString(idAttr);
	}

	public String getPath() {
		return this.path;
	}

	protected void setPath(String parent) {
		if (parent == null || parent.equals("")) {
			this.path = getId();
		} else {
			this.path = parent + ":" + getId();
		}
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this);
		for (Object o : getKnownAttributes()) {
			String key = (String) o;
			tsb.append(key, getAttributeAsString(key));
		}
		return tsb.toString();
	}

	public void setCollector(MetadataCollector metadataCollector) {
		this.mCollector = metadataCollector;
		for (MetadataType o : this.childTypes.keySet()) {
			Map<String, MetadataObject> map = this.childTypes.get(o);
			if (map == null) {
				continue;
			}
			Collection<MetadataObject> children = map.values();
			for (MetadataObject child : children) {
				child.setCollector(metadataCollector);
			}
		}
	}

	public abstract MetadataType[] getChildTypes();

	protected abstract String getIdAttr();

	/**
	 * Adds attributes to an attribute map.  This is called by the MetaObject
	 * constructor to initialize a map of atributes.  This map may be cached,
	 * so this method may not be called for every object construction.
	 *
	 * @param attributeMap Map to add attributes to
	 */
	protected abstract void addAttributesToMap(Map<String, AttrType<?>> attributeMap);

}

class CacheKey {
	private final Class<?> mClass;
	private final boolean strictParsing;

	public CacheKey(MetadataObject metadataObject, boolean strictParsing) {
		this.mClass = metadataObject.getClass();
		this.strictParsing = strictParsing;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CacheKey)) {
			return false;
		}
		CacheKey rhs = (CacheKey) obj;
		return new EqualsBuilder().append(this.mClass, rhs.mClass).append(this.strictParsing, rhs.strictParsing).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.mClass).append(this.strictParsing).toHashCode();
	}

}

