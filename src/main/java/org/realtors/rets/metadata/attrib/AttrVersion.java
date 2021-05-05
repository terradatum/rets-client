/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata.attrib;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataParseException;

/**
 * A version is a string formatted "major.minor.release".  This gets converted
 * to an integer such as major * 10,000,000 + minor * 100,000 + release.
 */
public class AttrVersion implements AttrType<Integer> {

	public Integer parse(String value, boolean strict) throws MetadataParseException {
		String[] parts = StringUtils.split(value, ".");
		int major, minor, release;
		if (strict && parts != null && parts.length != 3) {
			throw new MetadataParseException("Invalid version: " + value + ", " + parts.length + " parts");
		}
		try {
			major = Integer.parseInt(this.getPart(parts, 0));
			minor = Integer.parseInt(this.getPart(parts, 1));
			release = Integer.parseInt(this.getPart(parts, 2));
		} catch (NumberFormatException e) {
			throw new MetadataParseException("Invalid version: " + value, e);
		}
		if ((major < 100) && (major >= 0) && (minor < 100) && (minor >= 0) && (release < 100000) && (release >= 0)) {
			return major * 10000000 + minor * 100000 + release;
		}
		if (strict)
			throw new MetadataParseException("Invalid version: " + value);
		return 0;
	}

	private String getPart(String[] parts, int part) {
		if (parts != null && parts.length > part) return parts[part];
		return "0";
	}


	public String render(Object value) {
		if (value instanceof Integer) {
			int ver = (Integer) value;
			int release = ver % 100000;
			int minor = (ver / 100000) % 100;
			int major = (ver / 10000000);
			StringBuilder minstr = new StringBuilder(Integer.toString(minor));
			StringBuilder relstr = new StringBuilder(Integer.toString(release));
			while (minstr.length() < 2) {
				minstr.insert(0, "0");
			}
			while (relstr.length() < 5) {
				relstr.insert(0, "0");
			}
			return major + "." + minstr + "." + relstr;
		}
		return "0.0.0";
	}


	public Class<Integer> getType() {
		return Integer.class;
	}
}
