/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata.attrib;

import org.realtors.rets.metadata.AttrType;
import org.realtors.rets.metadata.MetadataParseException;

public class AttrBoolean implements AttrType<Boolean> {
	public Boolean parse(String value, boolean strict) throws MetadataParseException {
		if (value.equals("1")) {
			return Boolean.TRUE;
		}
		if (value.equals("0")) {
			return Boolean.FALSE;
		}

		if (value.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		}
		if (value.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		}

		if (value.equalsIgnoreCase("Y")) {
			return Boolean.TRUE;
		}
		if (value.equalsIgnoreCase("N")) {
			return Boolean.FALSE;
		}

		if (value.equals("")) {
			return Boolean.FALSE;
		}

		if (strict)
			throw new MetadataParseException("Invalid boolean value: " + value);
		return false;
	}

	public String render(Object value) {
		if (value instanceof Boolean && (Boolean) value) return "1";

		return "0";
	}

	public Class<Boolean> getType() {
		return Boolean.class;
	}
}
