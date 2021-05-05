/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata.attrib;

import org.realtors.rets.metadata.MetadataParseException;

public class AttrGenericText extends AttrAbstractText {
	private final String mChars;

	public AttrGenericText(int min, int max, String chars) {
		super(min, max);
		this.mChars = chars;
	}

	@Override
	protected void checkContent(String value) throws MetadataParseException {
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (this.mChars.indexOf(c) == -1) {
				throw new MetadataParseException("Invalid char (" + c + ") at position " + i);
			}
		}
	}

}
