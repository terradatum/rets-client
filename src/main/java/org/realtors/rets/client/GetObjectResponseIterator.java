package org.realtors.rets.client;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class GetObjectResponseIterator<T extends SingleObjectResponse> implements GetObjectIterator<T> {
	public static final char CR = '\r';
	public static final char LF = '\n';
	public static final String EOL = CR + "" + LF;
	public static final String BS = "--";

	private final PushbackInputStream multipartStream;
	private final String boundary;
	private Boolean hasNext;

	private GetObjectResponseIterator(GetObjectResponse<T> response, String boundary, int streamBufferSize) {
		this.boundary = boundary;

		BufferedInputStream input = new BufferedInputStream(response.getInputStream(), streamBufferSize);
		this.multipartStream = new PushbackInputStream(input, BS.length() + this.boundary.length() + EOL.length());
	}

	public static <T extends SingleObjectResponse> GetObjectIterator<T> createIterator(
			final GetObjectResponse<T> response,
			int streamBufferSize) throws Exception {
		String boundary = response.getBoundary();
		if (boundary != null)
			return new GetObjectResponseIterator<T>(response, boundary, streamBufferSize);

		return new GetObjectIterator<>() {

			public void close() throws IOException {
				response.getInputStream().close();
			}

			public boolean hasNext() {
				return false;
			}

			public T next() {
				throw new NoSuchElementException();
			}

			public void remove() {
				throw new UnsupportedOperationException("");
			}
		};
	}

	public boolean hasNext() {
		if (this.hasNext != null)
			return this.hasNext;

		try {
			this.hasNext = this.getHaveNext();
			return this.hasNext;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public T next() {
		if (!this.hasNext())
			throw new NoSuchElementException();

		this.hasNext = null;
		try {
			return getNext();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public void remove() {
		throw new UnsupportedOperationException();
	}


	public void close() throws IOException {
		this.multipartStream.close();
	}

	private boolean getHaveNext() throws IOException {
		String line;
		while ((line = this.readLine()) != null) {
			if (line.equals(BS + this.boundary))
				return true;
			if (line.equals(BS + this.boundary + BS))
				return false;
		}
		return false;
	}

	private T getNext() throws Exception {
		Map<String, String> headers = new HashMap<>();
		String header;
		while (StringUtils.isNotEmpty(header = this.readLine())) {
			int nvSeperatorIndex = header.indexOf(':');
			if (nvSeperatorIndex == -1) {
				headers.put(header, "");
			} else {
				String name = header.substring(0, nvSeperatorIndex);
				String value = header.substring(nvSeperatorIndex + 1).trim();
				headers.put(name, value);
			}
		}
		return (T) new SingleObjectResponse(headers, new SinglePartInputStream(this.multipartStream, BS + this.boundary));
	}

	// TODO find existing library to do this
	private String readLine() throws IOException {
		boolean eolReached = false;
		StringBuilder line = new StringBuilder();
		int currentChar = -1;
		while (!eolReached && (currentChar = this.multipartStream.read()) != -1) {
			eolReached = (currentChar == CR || currentChar == LF);
			if (!eolReached)
				line.append((char) currentChar);
		}

		if (currentChar == -1 && line.length() == 0)
			return null;

		if (currentChar == CR) {
			int nextChar = this.multipartStream.read();
			if (nextChar != LF)
				this.multipartStream.unread(new byte[]{(byte) nextChar});
		}

		return line.toString();
	}

}
