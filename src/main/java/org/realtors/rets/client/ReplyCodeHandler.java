package org.realtors.rets.client;

/**
 * @author jrayburn
 */
public interface ReplyCodeHandler {

	/**
	 * ReplyCodeHandler can choose to handle reply codes
	 * that are non-zero reply codes in its own fashion.
	 * <p>
	 * This is intended to be used to allow the SearchResultCollector
	 * to choose to throw InvalidReplyCodeException if the response is
	 * 20201 (Empty) or 20208 (MaxRowsExceeded).
	 *
	 * @param replyCode The RETS reply code
	 */
	void handleReplyCode(int replyCode);

}
