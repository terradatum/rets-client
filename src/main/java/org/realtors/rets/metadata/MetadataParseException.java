/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata;

public class MetadataParseException extends MetadataException {
	public MetadataParseException() {
		super();
	}

	public MetadataParseException(String msg) {
		super(msg);
	}

	public MetadataParseException(Throwable cause) {
		super(cause);
	}

	public MetadataParseException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
