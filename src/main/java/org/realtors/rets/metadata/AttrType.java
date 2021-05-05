/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata;

import java.io.Serializable;

public interface AttrType<T> extends Serializable {
	T parse(String value, boolean strict) throws MetadataParseException;

	Class<T> getType();

	default String render(Object value) {
		return value.toString();
	}
}
