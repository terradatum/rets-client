package org.realtors.rets.client;

import com.google.common.collect.Multimap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class LogoutResponse extends KeyValueResponse {
	private static final Log LOG = LogFactory.getLog(LogoutResponse.class);
	private static final String CONNECT_TIME_KEY = "ConnectTime";
	private static final String BILLING_KEY = "Billing";
	private static final String SIGNOFF_KEY = "SignOffMessage";

	private String seconds;
	private String billingInfo;
	private String logoutMessage;

	@Override
	protected void handleMultimap(Multimap<String, String> multimap) throws RetsException {
		for (Map.Entry<String, String> entry : multimap.entries()) {
			this.handleKeyValue(entry.getKey(), entry.getValue());
		}
	}

	private void handleKeyValue(String key, String value) throws RetsException {
		if (matchKey(key, CONNECT_TIME_KEY)) {
			this.seconds = value;
		} else if (matchKey(key, BILLING_KEY)) {
			this.billingInfo = value;
		} else if (matchKey(key, SIGNOFF_KEY)) {
			this.logoutMessage = value;
		} else {
			assertStrictWarning(LOG, "Invalid logout response key: " + key + " -> " + value);
		}
	}

	public String getSeconds() {
		return this.seconds;
	}

	public String getBillingInfo() {
		return this.billingInfo;
	}

	public String getLogoutMessage() {
		return this.logoutMessage;
	}

}
