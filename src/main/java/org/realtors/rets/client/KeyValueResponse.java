package org.realtors.rets.client;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

abstract public class KeyValueResponse {
	protected static final String CRLF = "\r\n";
	private static final Log LOG = LogFactory.getLog(KeyValueResponse.class);

	protected Document mDoc;
	protected int mReplyCode;
	protected boolean mStrict;

	public KeyValueResponse() {
		this.mStrict = false;
	}

	public void parse(InputStream stream, RetsVersion mVersion) throws RetsException {
		try {
			SAXBuilder builder = new SAXBuilder();
			this.mDoc = builder.build(stream);
			Element retsElement = this.mDoc.getRootElement();
			if (!retsElement.getName().equals("RETS")) {
				throw new RetsException("Expecting RETS");
			}

			int replyCode = NumberUtils.toInt(retsElement.getAttributeValue("ReplyCode"));
			this.mReplyCode = replyCode;
			if (!isValidReplyCode(replyCode)) {
				throw new InvalidReplyCodeException(replyCode);
			}
			Element capabilityContainer;
			if (RetsVersion.v1_0.equals(mVersion)) {
				capabilityContainer = retsElement;
			} else {
				List children = retsElement.getChildren();
				if (children.size() != 1) {
					throw new RetsException("Invalid number of children: " + children.size());
				}

				capabilityContainer = (Element) children.get(0);

				if (!capabilityContainer.getName().equals("RETS-RESPONSE")) {
					throw new RetsException("Expecting RETS-RESPONSE");
				}
			}
			this.handleRetsResponse(capabilityContainer);
		} catch (JDOMException | IOException e) {
			throw new RetsException(e);
		}
	}

	protected boolean isValidReplyCode(int replyCode) {
		return (ReplyCode.SUCCESS.equals(replyCode));

	}

	private void handleRetsResponse(Element retsResponse) throws RetsException {
		Map<String, String> map = new HashMap<>();
		Multimap<String, String> multimap = ArrayListMultimap.create();
		StringTokenizer tokenizer = new StringTokenizer(retsResponse.getText(), CRLF);
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			if (Strings.isNullOrEmpty(line)) continue;
			String[] splits = StringUtils.split(line, "=");
			String key = splits[0].trim();
			// guard against a missing value in a KeyValueResponse
			String value = splits.length > 1 ? splits[1].trim() : "";
			if (LOG.isDebugEnabled()) {
				LOG.debug("<" + key + "> -> <" + value + ">");
			}
			multimap.put(key, value);
		}
		this.handleMultimap(multimap);
	}

	protected abstract void handleMultimap(Multimap<String, String> multimap) throws RetsException;

	public boolean isStrict() {
		return this.mStrict;
	}

	public void setStrict(boolean strict) {
		this.mStrict = strict;
	}

	protected boolean matchKey(String key, String value) {
		if (this.mStrict)
			return key.equals(value);
		return key.equalsIgnoreCase(value);
	}

	protected void assertStrictWarning(Log log, String message) throws RetsException {
		if (this.mStrict)
			throw new RetsException(message);

		log.warn(message);
	}

}
