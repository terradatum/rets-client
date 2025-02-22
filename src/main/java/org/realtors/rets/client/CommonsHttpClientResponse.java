package org.realtors.rets.client;

import com.google.common.io.Closeables;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.realtors.rets.util.CaseInsensitiveTreeMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class CommonsHttpClientResponse implements RetsHttpResponse {
	private final HttpResponse response;
	private final Map<String, String> headers;
	private final Map<String, String> cookies;

	public CommonsHttpClientResponse(HttpResponse response, Map<String, String> cookies) {
		this.response = response;
		this.cookies = new CaseInsensitiveTreeMap<>(cookies);

		this.headers = new CaseInsensitiveTreeMap<>();
		for (Header header : this.response.getAllHeaders()) {
			this.headers.put(header.getName(), header.getValue());
		}
	}

	public int getResponseCode() {
		return this.response.getStatusLine().getStatusCode();
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public String getHeader(String header) {
		return this.headers.get(header);
	}


	public Map<String, String> getCookies() {
		return this.cookies;
	}


	public String getCookie(String cookie) {
		return this.cookies.get(cookie);
	}


	public String getCharset() {
		String contentType = StringUtils.trimToEmpty(this.getHeader(CommonsHttpClient.CONTENT_TYPE)).toLowerCase();
		String[] split = StringUtils.split(contentType, ";");
		if (split == null) return null;

		for (String s : split) {
			String sLower = s.toLowerCase().trim();
			boolean b = sLower.startsWith("charset=");
			if (b) {
				return StringUtils.substringAfter(s, "charset=");
			}
		}
		return null;
	}

	/**
	 * using this mess to provide logging, gzipping and httpmethod closing
	 */

	public InputStream getInputStream() throws RetsException {
		try {
			// get our underlying stream
			InputStream inputStream = this.response.getEntity().getContent();
			// gzipped aware checks
			String contentEncoding = StringUtils.trimToEmpty(this.getHeader(CommonsHttpClient.CONTENT_ENCODING)).toLowerCase();
			boolean gzipped = ArrayUtils.contains(CommonsHttpClient.DEFLATE_ENCODINGS.split(","), contentEncoding);
			if (gzipped) inputStream = new GZIPInputStream(inputStream);

			final InputStream in = inputStream;
			// the http method close wrapper (necessary)
			return new InputStream() {

				public int read() throws IOException {
					return in.read();
				}

				public int read(byte[] b) throws IOException {
					return in.read(b);
				}

				public int read(byte[] b, int off, int len) throws IOException {
					return in.read(b, off, len);
				}

				public void close() {
					// connection release _AFTER_ the input stream has been read
					try {
						/* TODO: migrate entire library to try-with-resources and new Apache HTTP client. Until then, leave this as-is */
						Closeables.close(in, true);
					} catch (IOException e) {
						// ignore
					}
				}
			};
		} catch (IOException e) {
			throw new RetsException(e);
		}
	}

}
