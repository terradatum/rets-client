package org.realtors.rets.metadata;

import org.realtors.rets.metadata.types.*;

import java.io.Serializable;

public class Metadata implements Serializable {

	protected RetsSystem system;

	public Metadata(MetadataCollector collector) throws MetadataException {
		MetadataObject[] sys = collector.getMetadata(MetadataType.SYSTEM, null);
		if (sys != null && sys.length == 1) {
			try {
				this.system = (RetsSystem) sys[0];
			} catch (ClassCastException e) {
				throw new MetadataException(e);
			}
			this.system.setCollector(collector);
		}
	}

	public Metadata(RetsSystem system) {
		this.system = system;
	}

	public RetsSystem getSystem() {
		return this.system;
	}

	public RetsResource getResource(String resourceId) {
		return this.system.getRetsResource(resourceId);
	}

	public RetsForeignKey getForeignKey(String foreignKeyId) {
		return this.system.getRetsForeignKey(foreignKeyId);
	}

	public RetsClass getRetsClass(String resourceId, String className) {
		RetsResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getRetsClass(className);
	}

	public RetsTable getTable(String resourceId, String className, String systemName) {
		RetsClass clazz = getRetsClass(resourceId, className);
		if (clazz == null) {
			return null;
		}
		return clazz.getRetsTable(systemName);
	}

	public RetsUpdate getUpdate(String resourceId, String className, String updateName) {
		RetsClass clazz = getRetsClass(resourceId, className);
		if (clazz == null) {
			return null;
		}
		return clazz.getRetsUpdate(updateName);
	}

	public RetsUpdateType getUpdateType(String resourceId, String className, String updateName, String systemName) {
		RetsUpdate update = getUpdate(resourceId, className, updateName);
		if (update == null) {
			return null;
		}
		return update.getRetsUpdateType(systemName);
	}

	public RetsObject getObject(String resourceId, String objectType) {
		RetsResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getRetsObject(objectType);
	}

	public RetsLookup getLookup(String resourceId, String lookupName) {
		RetsResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getRetsLookup(lookupName);
	}

	public RetsSearchHelp getSearchHelp(String resourceId, String searchHelpId) {
		RetsResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getRetsSearchHelp(searchHelpId);
	}

	public RetsValidationExternal getValidationExternal(String resourceId, String validationExternalName) {
		RetsResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getRetsValidationExternal(validationExternalName);
	}

	public RetsValidationLookup getValidationLookup(String resourceId, String validationLookupName) {
		RetsResource resource = getResource(resourceId);
		if (resource == null) {
			return null;
		}
		return resource.getRetsValidationLookup(validationLookupName);
	}

	private String getResourceId(MetadataObject obj) {
		String path = obj.getPath();
		int index = path.indexOf(':');
		if (index == -1) {
			return null;
		}
		return path.substring(0, index);
	}

	public RetsResource getResource(RetsTable field) {
		String resource = getResourceId(field);
		return getResource(resource);
	}

	public RetsLookup getLookup(RetsTable field) {
		String resource = getResourceId(field);
		return getLookup(resource, field.getLookupName());
	}

	public RetsSearchHelp getSearchHelp(RetsTable field) {
		String searchHelpID = field.getSearchHelpID();
		if (searchHelpID == null) {
			return null;
		}
		String resource = getResourceId(field);
		return getSearchHelp(resource, searchHelpID);
	}

	public RetsResource getResource(RetsClass clazz) {
		return getResource(getResourceId(clazz));
	}
}
