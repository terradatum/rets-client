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

public class AttrNumericPositive implements AttrType<Integer> {

	public Integer parse(String value, boolean strict) throws MetadataParseException {
		try {
			Integer integer = Integer.valueOf(value);
			if (strict && integer < 1) throw new IllegalArgumentException(String.format("%s is not positive", value));
			return integer;
		} catch (Exception e) {
			if (strict)
				throw new MetadataParseException(e);
			return 1;
		}
	}

	public Class<Integer> getType() {
		return Integer.class;
	}
}
