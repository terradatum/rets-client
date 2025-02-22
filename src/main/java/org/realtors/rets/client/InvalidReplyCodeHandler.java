package org.realtors.rets.client;

public interface InvalidReplyCodeHandler {
	InvalidReplyCodeHandler FAIL = new InvalidReplyCodeHandler() {
		public void invalidRetsReplyCode(int replyCode) throws InvalidReplyCodeException {
			throw new InvalidReplyCodeException(replyCode);
		}

		public void invalidRetsStatusReplyCode(int replyCode) throws InvalidReplyCodeException {
			throw new InvalidReplyCodeException(replyCode);
		}
	};

	void invalidRetsReplyCode(int replyCode) throws InvalidReplyCodeException;

	void invalidRetsStatusReplyCode(int replyCode) throws InvalidReplyCodeException;
}
