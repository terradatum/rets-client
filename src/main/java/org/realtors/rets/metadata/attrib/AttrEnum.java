/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata.attrib;

import org.realtors.rets.metadata.MetadataParseException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttrEnum extends AttrAbstractText {
	private Map<String, String> map;

	public AttrEnum(String[] values) {
		super(0, 0);
		this.map = new HashMap<>();
		for (String value : values) this.map.put(value, value);
		this.map = Collections.unmodifiableMap(this.map);
	}

	@Override
	protected void checkContent(String value) throws MetadataParseException {
		if (!this.map.containsKey(value))
			throw new MetadataParseException("Invalid key: " + value);
	}
}
