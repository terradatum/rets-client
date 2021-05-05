package org.realtors.rets.client;

import java.io.InputStream;
import java.util.Map;

/**
 * Interface for retrieving useful header fields from a RETS HTTP response
 */

public interface RetsHttpResponse {
	int getResponseCode();

	Map<String, String> getHeaders();

	String getHeader(String hdr);

	String getCookie(String cookie);

	String getCharset();

	InputStream getInputStream() throws RetsException;

	Map<String, String> getCookies();

}
