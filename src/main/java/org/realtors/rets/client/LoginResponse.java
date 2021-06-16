package org.realtors.rets.client;

import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LoginResponse extends KeyValueResponse {
	private static final String BROKER_KEY = "Broker";
	private static final String BROKER_CODE_KEY = "BrokerCode";
	private static final String BROKER_BRANCH_KEY = "BrokerBranch";
	private static final String MEMBER_NAME_KEY = "MemberName";
	private static final String METADATA_ID_KEY = "MetadataId";
	private static final String METADATA_VER_KEY = "MetadataVersion";
	private static final String MIN_METADATA_VER_KEY = "MinMetadataVersion";
	private static final String USER_INFO_KEY = "User";
	private static final String USER_ID_KEY = "UserId";
	private static final String USER_LEVEL_KEY = "UserLevel";
	private static final String USER_CLASS_KEY = "UserClass";
	private static final String AGENT_CODE_KEY = "AgentCode";
	private static final String OFFICE_LIST_KEY = "OfficeList";
	private static final String BALANCE_KEY = "Balance";
	private static final String TIMEOUT_KEY = "TimeoutSeconds";
	private static final String PWD_EXPIRE_KEY = "Expr";
	private static final String METADATA_TIMESTAMP_KEY = "MetadataTimestamp";
	private static final String MIN_METADATA_TIMESTAMP_KEY = "MinMetadataTimestamp";
	private static final String INFO_KEY = "Info";
	private static final String VENDOR_NAME_KEY = "VendorName";
	private static final String SERVER_PRODUCT_NAME_KEY = "ServerProductName";
	private static final String SERVER_PRODUCT_VERSION_KEY = "ServerProductVersion";
	private static final String OPERATOR_NAME = "OperatorName";
	private static final Log LOG = LogFactory.getLog(LoginResponse.class);
	private final CapabilityUrls capabilityUrls;
	private String sessionId;
	private String memberName;
	private String userId;
	private String userLevel;
	private String userClass;
	private String agentCode;
	private String brokerCode;
	private String brokerBranch;
	private String metadataVersion;
	private String minMetadataVersion;
	private String metadataTimestamp;
	private String minMetadataTimestamp;
	private String officeList;
	private String balance;
	private int sessionTimeout;
	private String passwordExpiration;
	private String vendorName;
	private String serverProductName;
	private String serverProductVersion;
	private String operatorName;
	private String metadataId;

	public LoginResponse(String loginUrl) {
		super();
		URL url = null;
		try {
			url = new URL(loginUrl);
		} catch (MalformedURLException e) {
			LOG.warn("Bad URL: " + loginUrl);
		}
		this.capabilityUrls = new CapabilityUrls(url);
	}

	public LoginResponse() {
		super();
		this.capabilityUrls = new CapabilityUrls();
	}

	@Override
	public void parse(InputStream stream, RetsVersion version) throws RetsException {
		super.parse(stream, version);
		if (ReplyCode.BROKER_CODE_REQUIRED.equals(this.mReplyCode)) {
			throw new BrokerCodeRequiredException(getBroker());
		}
	}

	@Override
	protected boolean isValidReplyCode(int replyCode) {
		return (super.isValidReplyCode(replyCode) || ReplyCode.BROKER_CODE_REQUIRED.equals(replyCode));
	}

	@Override
	protected void handleMultimap(Multimap<String, String> multimap) throws RetsException {
		for (Map.Entry<String, String> entry : multimap.entries()) {
			if (!entry.getKey().equals(INFO_KEY)) {
				this.handleKeyValue(entry.getKey(), entry.getValue());
			}
		}
		for (Map.Entry<String, String> entry : multimap.entries()) {
			if (entry.getKey().equals(INFO_KEY)) {
				this.handleKeyValue(entry.getKey(), entry.getValue());
			}
		}
	}

	private void handleKeyValue(String key, String value) throws RetsException {
		if (matchKey(key, BROKER_KEY)) {
			String[] strings = StringUtils.split(value, ",");
			if (strings.length > 0 && strings.length < 3) {
				this.brokerCode = strings[0].trim();
				if (strings.length > 1) {
					this.brokerBranch = strings[1].trim();
				}
			} else if (ReplyCode.BROKER_CODE_REQUIRED.equals(this.mReplyCode)) {
				throw new RetsException("Invalid broker/branch code: " + value);
			}
		} else if (matchKey(key, BROKER_CODE_KEY)) {
			this.brokerCode = value;
		} else if (matchKey(key, BROKER_BRANCH_KEY)) {
			this.brokerBranch = value;
		} else if (matchKey(key, MEMBER_NAME_KEY)) {
			this.memberName = value;
		} else if (matchKey(key, METADATA_ID_KEY)) {
			this.metadataId = value;
		} else if (matchKey(key, METADATA_VER_KEY)) {
			this.metadataVersion = value;
		} else if (matchKey(key, MIN_METADATA_VER_KEY)) {
			this.minMetadataVersion = value;
		} else if (matchKey(key, METADATA_TIMESTAMP_KEY)) {
			this.metadataTimestamp = value;
		} else if (matchKey(key, MIN_METADATA_TIMESTAMP_KEY)) {
			this.minMetadataTimestamp = value;
		} else if (matchKey(key, USER_INFO_KEY)) {
			String[] strings = StringUtils.split(value, ",");
			if (strings.length > 0 && strings.length < 5) {
				this.userId = strings[0].trim();
				if (strings.length > 1) {
					this.userClass = strings[1].trim();
				}
				if (strings.length > 2) {
					this.userLevel = strings[2].trim();
				}
				if (strings.length > 3) {
					this.agentCode = strings[3].trim();
				}
			}
		} else if (matchKey(key, USER_ID_KEY)) {
			this.userId = value;
		} else if (matchKey(key, USER_LEVEL_KEY)) {
			this.userLevel = value;
		} else if (matchKey(key, USER_CLASS_KEY)) {
			this.userClass = value;
		} else if (matchKey(key, AGENT_CODE_KEY)) {
			this.agentCode = value;
		} else if (matchKey(key, OFFICE_LIST_KEY)) {
			this.officeList = value;
		} else if (matchKey(key, BALANCE_KEY)) {
			this.balance = value;
		} else if (matchKey(key, TIMEOUT_KEY)) {
			this.sessionTimeout = NumberUtils.toInt(value);
		} else if (matchKey(key, PWD_EXPIRE_KEY)) {
			this.passwordExpiration = value;
		} else if (matchKey(key, VENDOR_NAME_KEY)) {
			this.vendorName = value;
		} else if (matchKey(key, SERVER_PRODUCT_NAME_KEY)) {
			this.serverProductName = value;
		} else if (matchKey(key, SERVER_PRODUCT_VERSION_KEY)) {
			this.serverProductVersion = value;
		} else if (matchKey(key, OPERATOR_NAME)) {
			this.operatorName = value;
		} else if (matchKey(key, CapabilityUrls.ACTION_URL)) {
			this.capabilityUrls.setActionUrl(value);
		} else if (matchKey(key, CapabilityUrls.CHANGE_PASSWORD_URL)) {
			this.capabilityUrls.setChangePasswordUrl(value);
		} else if (matchKey(key, CapabilityUrls.GET_OBJECT_URL)) {
			this.capabilityUrls.setGetObjectUrl(value);
		} else if (matchKey(key, CapabilityUrls.LOGIN_URL)) {
			this.capabilityUrls.setLoginUrl(value);
		} else if (matchKey(key, CapabilityUrls.LOGIN_COMPLETE_URL)) {
			this.capabilityUrls.setLoginCompleteUrl(value);
		} else if (matchKey(key, CapabilityUrls.LOGOUT_URL)) {
			this.capabilityUrls.setLogoutUrl(value);
		} else if (matchKey(key, CapabilityUrls.SEARCH_URL)) {
			this.capabilityUrls.setSearchUrl(value);
		} else if (matchKey(key, CapabilityUrls.GET_METADATA_URL)) {
			this.capabilityUrls.setGetMetadataUrl(value);
		} else if (matchKey(key, CapabilityUrls.UPDATE_URL)) {
			this.capabilityUrls.setUpdateUrl(value);
		} else if (matchKey(key, CapabilityUrls.SERVER_INFO_URL)) {
			this.capabilityUrls.setServerInfo(value);
			LOG.warn("Deprecated: " + key + " -> " + value);
		} else if (matchKey(key, "Get")) {
			LOG.warn("Found bad key: Get -> " + value);
		} else if (matchKey(key, INFO_KEY)) {
			String[] split = value.split(";");
			handleKeyValue(split[0], split[2]);
		} else {
			if (key.substring(0, 2).equalsIgnoreCase("X-")) {
				LOG.warn("Unknown experimental key: " + key + " -> " + value);
			} else {
				assertStrictWarning(LOG, "Invalid login response key: " + key + " -> " + value);
			}
		}
	}

	public String getMemberName() {
		return this.memberName;
	}

	public String getUserInformation() {
		return
				Arrays.stream(new String[] {
						this.userId,
						this.userClass,
						this.userLevel,
						this.agentCode})
						.filter(StringUtils::isNotBlank)
						.collect(Collectors.joining(","));
	}

	public String getUserId() {
		return this.userId;
	}

	public String getUserLevel() {
		return this.userLevel;
	}

	public String getUserClass() {
		return this.userClass;
	}

	public String getAgentCode() {
		return this.agentCode;
	}

	public String getBroker() {
		return
				Arrays.stream(new String[] {
						this.brokerCode,
						this.brokerBranch})
						.filter(StringUtils::isNotBlank)
						.collect(Collectors.joining(","));

	}

	public String getBrokerCode() {
		return this.brokerCode;
	}

	public String getBrokerBranch() {
		return this.brokerBranch;
	}

	public String getMetadataVersion() {
		return this.metadataVersion;
	}

	public String getMinMetadataVersion() {
		return this.minMetadataVersion;
	}

	public String getMetadataTimestamp() {
		return this.metadataTimestamp;
	}

	public String getMinMetadataTimestamp() {
		return this.minMetadataTimestamp;
	}

	public String getOfficeList() {
		return this.officeList;
	}

	public String getBalance() {
		return this.balance;
	}

	public int getSessionTimeout() {
		return this.sessionTimeout;
	}

	public String getPasswordExpiration() {
		return this.passwordExpiration;
	}

	public CapabilityUrls getCapabilityUrls() {
		return this.capabilityUrls;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public String getServerProductVersion() {
		return serverProductVersion;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public String getServerProductName() {
		return serverProductName;
	}

	public String getMetadataId() {
		return metadataId;
	}
}
