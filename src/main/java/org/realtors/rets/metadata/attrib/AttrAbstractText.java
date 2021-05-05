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

public abstract class AttrAbstractText implements AttrType<String> {
	protected final int min;
	protected final int max;

	public AttrAbstractText(int min, int max) {
		this.min = min;
		this.max = max;
	}


	public String parse(String value, boolean strict) throws MetadataParseException {
		if (!strict)
			return value;
		int l = value.length();
		if (this.min != 0 && l < this.min) {
			throw new MetadataParseException("Value too short (min " + this.min + "): " + l);
		}
		if (this.max != 0 && l > this.max) {
			throw new MetadataParseException("Value too long (max " + this.max + "): " + l);
		}
		checkContent(value);
		return value;
	}


	public Class<String> getType() {
		return String.class;
	}

	protected abstract void checkContent(String value) throws MetadataParseException;

}
