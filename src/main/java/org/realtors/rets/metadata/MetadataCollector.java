/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata;

import java.io.Serializable;

/**
 * Interface for Metadata objects to collect their children.
 */
public interface MetadataCollector extends Serializable {
	/**
	 * @param path path to the parent object.
	 */
	MetadataObject[] getMetadata(MetadataType type, String path);

	MetadataObject[] getMetadataRecursive(MetadataType type, String path);
}
