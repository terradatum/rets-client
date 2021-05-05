package org.realtors.rets.client;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class GetObjectRequest extends VersionInsensitiveRequest {
	public static final String KEY_RESOURCE = "Resource";
	public static final String KEY_TYPE = "Type";
	public static final String KEY_LOCATION = "Location";
	public static final String KEY_ID = "ID";

	private final Map<String, Object> mMap;

	public GetObjectRequest(String resource, String type) {
		this(resource, type, new String[]{"*/*"});
	}

	public GetObjectRequest(String resource, String type, String[] acceptMimeTypes) {
		setQueryParameter(KEY_RESOURCE, resource);
		setQueryParameter(KEY_TYPE, type);
		this.mMap = new HashMap<>();
		setHeader("Accept", StringUtils.join(acceptMimeTypes, ", "));
	}

	@Override
	public void setUrl(CapabilityUrls urls) {
		setUrl(urls.getGetObjectUrl());
	}

	public void setLocationOnly(boolean flag) {
		if (flag) {
			setQueryParameter(KEY_LOCATION, "1");
		} else {
			setQueryParameter(KEY_LOCATION, null);
		}
	}

	public void addObject(String resourceEntity, String id) {
		if (id == null)
			throw new NullPointerException("Object id should not be null. "
					+ "Cannot remove object already added to request.");

		Object cur = this.mMap.get(resourceEntity);
		if (id.equals("*")) {
			this.mMap.put(resourceEntity, id);
		} else if (cur == null) {
			this.mMap.put(resourceEntity, id);
		} else if (cur instanceof String) {
			if (ObjectUtils.equals(cur, "*")) {
				return;
			}
			if (ObjectUtils.equals(cur, id)) {
				return;
			}
			Set<Object> s = new HashSet<>();
			s.add(cur);
			s.add(id);
			this.mMap.put(resourceEntity, s);
		} else if (cur instanceof Set<?>) {
			//noinspection unchecked
			((Set<Object>) cur).add(id);
		} else {
			/* NOTREACHED */
			throw new RuntimeException(resourceEntity + " has invalid value " + "of type " + cur.getClass().getName());
		}
		setQueryParameter(KEY_ID, makeIdStr());
	}

	private String makeIdStr() {
		StringBuilder id = new StringBuilder();
		Iterator<String> iter = this.mMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			id.append(key);
			Object cur = this.mMap.get(key);
			if (cur instanceof String) {
				id.append(":");
				id.append(cur);
			} else if (cur instanceof Set) {
				for (Object o : ((Set<?>) cur).toArray()) {
					String val = (String) o;
					id.append(":");
					id.append(val);
				}
			} else {
				throw new RuntimeException(key + " has invalid value of " + "type " + cur.getClass().getName());
			}
			if (iter.hasNext()) {
				id.append(",");
			}
		}
		return id.toString();
	}
}
