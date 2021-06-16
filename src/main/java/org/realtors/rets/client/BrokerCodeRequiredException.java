/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.client;

/**
 * dbt is lame and hasn't overridden the default
 * javadoc string.
 */
public class BrokerCodeRequiredException extends RetsException {

	public BrokerCodeRequiredException(String broker) {
		super(String.format("Broker Code Required - current values: %s", broker));
	}
}
