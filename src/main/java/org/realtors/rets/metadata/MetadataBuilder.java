/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.metadata;

import org.realtors.rets.metadata.types.*;

public abstract class MetadataBuilder {
	private boolean mStrict;

	protected MetadataBuilder() {
		this.mStrict = false;
	}

	protected static void setAttribute(MetadataObject obj, String key, String value) {
		obj.setAttribute(key, value);
	}

	public boolean isStrict() {
		return this.mStrict;
	}

	public void setStrict(boolean strict) {
		this.mStrict = strict;
	}

	protected Metadata finish(RetsSystem system) {
		return new Metadata(system);
	}

	protected RetsSystem buildSystem() {
		return new RetsSystem(this.mStrict);
	}

	protected RetsResource buildResource() {
		return new RetsResource(this.mStrict);
	}

	protected RetsForeignKey buildForeignKey() {
		return new RetsForeignKey(this.mStrict);
	}

	protected RetsClass buildClass() {
		return new RetsClass(this.mStrict);
	}

	protected RetsTable buildTable() {
		return new RetsTable(this.mStrict);
	}

	protected RetsUpdate buildUpdate() {
		return new RetsUpdate(this.mStrict);
	}

	protected RetsUpdateType buildUpdateType() {
		return new RetsUpdateType(this.mStrict);
	}

	protected RetsObject buildObject() {
		return new RetsObject(this.mStrict);
	}

	protected RetsSearchHelp buildSearchHelp() {
		return new RetsSearchHelp(this.mStrict);
	}

	protected RetsEditMask buildEditMask() {
		return new RetsEditMask(this.mStrict);
	}

	protected RetsLookup buildLookup() {
		return new RetsLookup(this.mStrict);
	}

	protected RetsLookupType buildLookupType() {
		return new RetsLookupType(this.mStrict);
	}

	protected RetsUpdateHelp buildUpdateHelp() {
		return new RetsUpdateHelp(this.mStrict);
	}

	protected RetsValidationLookup buildValidationLookup() {
		return new RetsValidationLookup(this.mStrict);
	}

	protected RetsValidationExternalType buildValidationExternalType() {
		return new RetsValidationExternalType(this.mStrict);
	}

	protected RetsValidationExpression buildValidationExpression() {
		return new RetsValidationExpression(this.mStrict);
	}

	protected RetsValidationExternal buildValidationExternal() {
		return new RetsValidationExternal(this.mStrict);
	}

	protected RetsValidationLookupType buildValidationLookupType() {
		return new RetsValidationLookupType(this.mStrict);
	}

	public abstract Metadata doBuild(Object src) throws MetadataException;

	public abstract MetadataObject[] parse(Object src) throws MetadataException;

	protected MetadataObject newType(MetadataType type) {
		if (type == MetadataType.SYSTEM) {
			return buildSystem();
		}
		if (type == MetadataType.RESOURCE) {
			return buildResource();
		}
		if (type == MetadataType.FOREIGNKEYS) {
			return buildForeignKey();
		}
		if (type == MetadataType.CLASS) {
			return buildClass();
		}
		if (type == MetadataType.TABLE) {
			return buildTable();
		}
		if (type == MetadataType.UPDATE) {
			return buildUpdate();
		}
		if (type == MetadataType.UPDATE_TYPE) {
			return buildUpdateType();
		}
		if (type == MetadataType.OBJECT) {
			return buildObject();
		}
		if (type == MetadataType.SEARCH_HELP) {
			return buildSearchHelp();
		}
		if (type == MetadataType.EDITMASK) {
			return buildEditMask();
		}
		if (type == MetadataType.UPDATE_HELP) {
			return buildUpdateHelp();
		}
		if (type == MetadataType.LOOKUP) {
			return buildLookup();
		}
		if (type == MetadataType.LOOKUP_TYPE) {
			return buildLookupType();
		}
		if (type == MetadataType.VALIDATION_LOOKUP) {
			return buildValidationLookup();
		}
		if (type == MetadataType.VALIDATION_LOOKUP_TYPE) {
			return buildValidationLookupType();
		}
		if (type == MetadataType.VALIDATION_EXTERNAL) {
			return buildValidationExternal();
		}
		if (type == MetadataType.VALIDATION_EXTERNAL_TYPE) {
			return buildValidationExternalType();
		}
		if (type == MetadataType.VALIDATION_EXPRESSION) {
			return buildValidationExpression();
		}
		throw new RuntimeException("No metadata type class found for " + type.name());
	}
}
