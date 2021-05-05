package org.realtors.rets.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class IgnoreInvalidReplyCodeHandler implements InvalidReplyCodeHandler {
	private static final Log LOG = LogFactory.getLog(IgnoreInvalidReplyCodeHandler.class);
	private final List<Integer> ignoreReplyCodes;

	public IgnoreInvalidReplyCodeHandler(List<Integer> ignoreReplyCodes) {
		this.ignoreReplyCodes = ignoreReplyCodes;
	}

	@Override
	public void invalidRetsReplyCode(int replyCode) throws InvalidReplyCodeException {
		if (!ignoreReplyCodes.contains(replyCode)) {
			throw new InvalidReplyCodeException(replyCode);
		}
		LOG.warn(String.format("Ignoring RETS Reply Code: %s", ReplyCode.fromValue(replyCode)));
	}

	@Override
	public void invalidRetsStatusReplyCode(int replyCode) throws InvalidReplyCodeException {
		this.invalidRetsReplyCode(replyCode);
	}
}
