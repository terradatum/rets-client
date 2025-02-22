package org.realtors.rets.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Handles XML parsing from response setting the proper fields using a SearchResultCollector
 */
public class SearchResultHandler implements ContentHandler, ErrorHandler {
	private static final Log LOG = LogFactory.getLog(SearchResultHandler.class);
	private static SAXParserFactory FACTORY = SAXParserFactory.newInstance();
	private final SearchResultCollector collector;
	private final InvalidReplyCodeHandler invalidReplyCodeHandler;
	private final CompactRowPolicy compactRowPolicy;
	private int dataCount;
	private StringBuffer currentEntry;
	private String delimiter;
	private Locator locator;
	private String[] columns;

	public SearchResultHandler(SearchResultCollector r) {
		this(r, InvalidReplyCodeHandler.FAIL, CompactRowPolicy.DEFAULT);
	}

	public SearchResultHandler(SearchResultCollector r, SAXParserFactory factory) {
		this(r);
		FACTORY = factory;
	}

	public SearchResultHandler(SearchResultCollector r, InvalidReplyCodeHandler invalidReplyCodeHandler, CompactRowPolicy badRowPolicy) {
		this.compactRowPolicy = badRowPolicy;
		if (r == null)
			throw new NullPointerException("SearchResultCollector must not be null");

		if (invalidReplyCodeHandler == null)
			throw new NullPointerException("InvalidReplyCodeHandler must not be null");

		if (badRowPolicy == null)
			throw new NullPointerException("BadRowPolicy must not be null");

		this.collector = r;
		this.dataCount = 0;
		this.invalidReplyCodeHandler = invalidReplyCodeHandler;
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		String name = localName;
		if (localName.equals("")) {
			name = qName;
		}
		if (name.equals("RETS") || name.equals("RETS-STATUS")) {
			String rawrepcode = atts.getValue("ReplyCode");
			try {
				int repcode = Integer.parseInt(rawrepcode);
				if (repcode > 0) {
					try {
						if (ReplyCode.MAXIMUM_RECORDS_EXCEEDED.equals(repcode))
							return;

						if (ReplyCode.NO_RECORDS_FOUND.equals(repcode))
							return;

						if (name.equals("RETS"))
							this.invalidReplyCodeHandler.invalidRetsReplyCode(repcode);
						else
							this.invalidReplyCodeHandler.invalidRetsStatusReplyCode(repcode);
					} catch (InvalidReplyCodeException e) {
						String text = atts.getValue("", "ReplyText");
						e.setRemoteMessage(text);
						throw new SAXException(e);
					}
				}
			} catch (NumberFormatException e) {
				throw new SAXParseException("Invalid ReplyCode '" + rawrepcode + "'", this.locator);
			}
			return;
		}
		if (name.equals("COUNT")) {
			String s = atts.getValue("Records");
			if (s == null) {
				s = atts.getValue("", "Records");
				if (s == null) {
					throw new SAXParseException("COUNT tag has no Records " + "attribute", this.locator);
				}
			}
			int i = Integer.parseInt(s, 10);
			this.collector.setCount(i);
			return;
		}
		if (name.equals("DELIMITER")) {
			String s = atts.getValue("value");
			if (s == null) {
				s = atts.getValue("", "value");
				if (s == null) {
					throw new RuntimeException("Invalid Delimiter");
				}
			}
			int i = Integer.parseInt(s, 16);
			this.delimiter = "" + (char) i;
			return;
		}
		if (name.equals("COLUMNS") || name.equals("DATA")) {
			this.currentEntry = new StringBuffer();
			return;
		}
		if (name.equals("MAXROWS")) {
			this.collector.setMaxRows();
			return;
		}
		// Unknown tag. danger, will.
		LOG.warn("Unknown tag: " + name + ", qName = " + qName);

	}

	public void characters(char[] ch, int start, int length) {
		if (this.currentEntry != null) {
			this.currentEntry.append(ch, start, length);
		}
	}

	public void ignorableWhitespace(char[] ch, int start, int length) {
		// we ignore NOZINK!
		characters(ch, start, length);
	}

	/**
	 * do NOT use string.split() unless your prepared to deal with loss due to token boundary conditions
	 */
	private String[] split(String input) throws SAXParseException {
		if (this.delimiter == null) {
			throw new SAXParseException("Invalid compact format - DELIMITER not specified", this.locator);
		}
		if (!input.startsWith(this.delimiter)) {
			throw new SAXParseException("Invalid compact format", this.locator);
		}
		StringTokenizer tkn = new StringTokenizer(input, this.delimiter, true);
		List list = new LinkedList();
		tkn.nextToken(); // junk the first element
		String last = null;
		while (tkn.hasMoreTokens()) {
			String next = tkn.nextToken();
			if (next.equals(this.delimiter)) {
				if (last == null) {
					list.add("");
				} else {
					last = null;
				}
			} else {
				list.add(next);
				last = next;
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	public void endElement(String uri, String localName, String qName) throws SAXParseException {
		String name = localName;
		if (name.equals("")) {
			name = qName;
		}
		if (name.equals("COLUMNS") || name.equals("DATA")) {
			String[] contents = split(this.currentEntry.toString());
			if (name.equals("COLUMNS")) {
				this.collector.setColumns(contents);
				this.columns = contents;
			} else {
				if (this.compactRowPolicy.apply(this.dataCount, this.columns, contents)) {
					this.dataCount++;
					this.collector.addRow(contents);
				}
			}
			this.currentEntry = null;
		}
	}

	public void startDocument() {
		LOG.debug("Start document");
	}

	public void endDocument() {
		LOG.debug("Document ended");
		this.collector.setComplete();
	}

	public void startPrefixMapping(String prefix, String uri) {
		// LOG.debug("prefix mapping: " + prefix);
	}

	public void endPrefixMapping(String prefix) {
		// LOG.debug("prefix mapping: " + prefix);
	}

	public void processingInstruction(String target, String data) throws SAXException {
		throw new SAXException("processing instructions not supported: " + "target=" + target + ", data=" + data);
	}

	public void skippedEntity(String name) throws SAXException {
		throw new SAXException("skipped entities not supported: name=" + name);
	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public void error(SAXParseException e) throws SAXException {
		throw e;
	}

	public void fatalError(SAXParseException e) throws SAXException {
		throw e;
	}

	public void warning(SAXParseException e) {
		LOG.warn("an error occurred while parsing.  Attempting to continue", e);
	}


	public void parse(InputSource src) throws RetsException {
		parse(src, null);
	}

	/**
	 * created in order to pass the charset to the parser for proper encoding
	 *
	 * @param str
	 * @param charset
	 * @throws RetsException
	 */

	public void parse(InputStream str, String charset) throws RetsException {
		parse(new InputSource(str), charset);
		try {
			str.close();
		} catch (IOException e) {
			throw new RetsException(e);
		}
	}

	/**
	 * Pareses given source with the given charset
	 *
	 * @param src
	 * @throws RetsException
	 */
	public void parse(InputSource src, String charset) throws RetsException {
		String encoding = src.getEncoding();
		if (encoding == null && (charset != null)) {
			encoding = charset;
			LOG.debug("Charset from headers:" + charset + ". Setting as correct encoding for parsing");
			src.setEncoding(encoding);
		}
		try {
			SAXParser p = FACTORY.newSAXParser();
			XMLReader r = p.getXMLReader();
			r.setContentHandler(this);
			r.setErrorHandler(this);
			r.parse(src);
		} catch (SAXException se) {
			if (se.getException() != null && se.getException() instanceof RetsException) {
				throw (RetsException) se.getException();
			}
			throw new RetsException(se);
		} catch (Exception e) {
			LOG.error("An exception occurred", e);
			throw new RetsException(e);

		}
	}
}
