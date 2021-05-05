package org.realtors.rets.client;

import org.realtors.rets.util.CaseInsensitiveTreeMap;

import java.io.InputStream;
import java.util.Map;

/**
 * Representation of a single object returned
 * from a RETS server.
 *
 * @author jrayburn
 */
public class SingleObjectResponse {

	public static final String CONTENT_TYPE = "Content-Type";
	public static final String LOCATION = "Location";
	public static final String CONTENT_DESCRIPTION = "Content-Description";
	public static final String OBJECT_ID = "Object-ID";
	public static final String CONTENT_ID = "Content-ID";

	private final Map<String, String> headers;
	private final InputStream inputStream;

	public SingleObjectResponse(Map<String, String> headers, InputStream in) {
		this.headers = new CaseInsensitiveTreeMap<>(headers);
		this.inputStream = in;
	}

	public String getType() {
		return this.headers.get(CONTENT_TYPE);
	}

	public String getContentID() {
		return this.headers.get(CONTENT_ID);
	}

	public String getObjectID() {
		return this.headers.get(OBJECT_ID);
	}

	public String getDescription() {
		return this.headers.get(CONTENT_DESCRIPTION);
	}

	public String getLocation() {
		return this.headers.get(LOCATION);
	}

	public InputStream getInputStream() {
		return this.inputStream;
	}
}
