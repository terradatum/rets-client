package org.realtors.rets.client;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.metadata.Metadata;
import org.realtors.rets.metadata.MetadataException;

import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * RetsSession is the core class of the rets.client package.
 */
public class RetsSession {
	private static final Log LOG = LogFactory.getLog(RetsSession.class);
	private static String userAgent = "rets-client/1.2";
	private final RetsHttpClient httpClient;
	private final RetsTransport transport;
	private CapabilityUrls capabilityUrls;
	private String sessionId;
	public static final String USER_AGENT = "User-Agent";


	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 * <p>
	 * Uses a  default implementation of RetsHttpClient based on
	 * apache commons http client.
	 * <p>
	 * Uses the RetsVersion.RETS_DEFAULT as the RetsVersion for
	 * this session.
	 * <p>
	 * Uses sAgent at the User-Agent setting for this RetsSession.
	 *
	 * @param loginUrl URL of the Login transaction.
	 */
	public RetsSession(String loginUrl) {
		this(loginUrl, new CommonsHttpClient());
	}

	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 * <p>
	 * Uses the RetsVersion.RETS_DEFAULT as the RetsVersion for
	 * this session.
	 * <p>
	 * Uses sAgent at the User-Agent setting for this RetsSession.
	 *
	 * @param loginUrl   URL of the Login transaction
	 * @param httpClient a RetsHttpClient implementation.  The default
	 *                   is CommonsHttpClient.
	 */
	public RetsSession(String loginUrl, RetsHttpClient httpClient) {
		this(loginUrl, httpClient, RetsVersion.DEFAULT);
	}

	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 * <p>
	 * Uses sAgent at the User-Agent setting for this RetsSession.
	 *
	 * @param loginUrl    URL of the Login transaction
	 * @param httpClient  a RetsHttpClient implementation.  The default
	 *                    is CommonsHttpClient.
	 * @param retsVersion The RetsVersion used by this RetsSession.
	 */
	public RetsSession(String loginUrl, RetsHttpClient httpClient, RetsVersion retsVersion) {
		this(loginUrl, httpClient, retsVersion, null, false);
	}

	/**
	 * Creates a new <code>RetsSession</code> instance.
	 * You must call login(user, pass) before attempting any other
	 * transactions.
	 *
	 * @param loginUrl    URL of the Login transaction
	 * @param httpClient  a RetsHttpClient implementation.  The default
	 *                    is CommonsHttpClient.
	 * @param retsVersion The RetsVersion used by this RetsSession.
	 * @param userAgent   specific User-Agent to use for this session.
	 */
	public RetsSession(String loginUrl,
	                   RetsHttpClient httpClient,
	                   RetsVersion retsVersion,
	                   String userAgent,
	                   boolean strict) {
		final Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("rets-client.properties"));
			RetsSession.userAgent = properties.getProperty("user-agent") + "/" + properties.getProperty("version");
		} catch (IOException e) {
			// do nothing, use the default static value
		}
		if (Strings.isNullOrEmpty(userAgent)) {
			userAgent = RetsSession.userAgent;
		}
		this.capabilityUrls = new CapabilityUrls();
		this.capabilityUrls.setLoginUrl(loginUrl);

		this.httpClient = httpClient;
		this.httpClient.addDefaultHeader(USER_AGENT, userAgent);
		this.transport = new RetsTransport(httpClient, this.capabilityUrls, retsVersion, strict);
	}

	/**
	 * Sets the default User-Agent value for RetsSessions created without
	 * a specified User-Agent value.
	 *
	 * @param userAgent Default User-Agent value to use for all RetsSession
	 *                  objects created in the future.
	 */
	public static void setUserAgent(String userAgent) {
		RetsSession.userAgent = userAgent;
	}

	/**
	 * Query the current RetsVersion being used in this session.
	 * <p>
	 * Initially, this will be the value passed to the RetsTransport.
	 * However, if during auto-negotiation the RetsTransport changes
	 * the RetsSession, this value may change throughout the session.
	 *
	 * @return the current RetsVersion value being used by the
	 * RetsTransport.
	 */
	public RetsVersion getRetsVersion() {
		return this.transport.getRetsVersion();
	}

	/**
	 * Get the current RETS Session ID
	 *
	 * @return the current RETS Session ID or null is the server has
	 * not specified one
	 */
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		LOG.debug("setting Session-ID to: " + sessionId);
		this.sessionId = sessionId;
	}

	public void setMonitor(NetworkEventMonitor monitor) {
		this.transport.setMonitor(monitor);
	}

	public boolean isStrict() {
		return this.transport.isStrict();
	}

	public void setStrict(boolean strict) {
		this.transport.setStrict(strict);
	}

	public String getLoginUrl() {
		return this.capabilityUrls.getLoginUrl();
	}

	public Metadata getIncrementalMetadata() throws RetsException {
		try {
			return new Metadata(new SimpleMetadataCollector(this.transport));
		} catch (MetadataException e) {
			throw new RetsException(e);
		}
	}

	/**
	 * Get the complete RETS metadata.
	 *
	 * @return The RETS metadata object for these credentials.
	 * @throws RetsException
	 */
	public Metadata getMetadata() throws RetsException {
		return this.transport.getMetadata("null");
	}

	/**
	 * Ability to download the raw metadata to a location
	 *
	 * @param location
	 * @return
	 * @throws RetsException
	 */
	public Metadata getMetadata(String location) throws RetsException {
		return this.transport.getMetadata(location);
	}

	/**
	 * Perform a low level GetMetadatRequest.  To retrieve
	 * structured metadata,
	 *
	 * @param req GetMetadataRequest
	 * @return GetMetadataResponse, containing all MetaObjects
	 * returned
	 * @throws RetsException if an error occurs
	 * @see #getMetadata()
	 */
	public GetMetadataResponse getMetadata(GetMetadataRequest req) throws RetsException {
		return this.transport.getMetadata(req);
	}

	/**
	 * Fetches the action (MOTD) from the server.
	 *
	 * @throws RetsException if an error occurs
	 */
	private void getAction() throws RetsException {
		String actionUrl = this.capabilityUrls.getActionUrl();
		if (actionUrl == null) {
			LOG.warn("No Action-URL available, skipping");
			return;
		}
		GenericHttpRequest actionRequest = new GenericHttpRequest(actionUrl) {
			@Override
			public Map<String, String> getHeaders() {
				return null;
			}
		};
		RetsHttpResponse httpResponse = this.httpClient.doRequest("GET", actionRequest);
		try {
			httpResponse.getInputStream().close();
		} catch (Exception e) {
			LOG.error("Action URL weirdness", e);
		}
	}

	/**
	 * Implementation that allow for single or multi-part
	 * GetObject requests.
	 *
	 * @param req
	 * @return
	 * @throws RetsException if an error occurs
	 */
	public GetObjectResponse<SingleObjectResponse> getObject(GetObjectRequest req) throws RetsException {
		return this.transport.getObject(req);
	}

	/**
	 * @param resource
	 * @param type
	 * @param entity
	 * @param id
	 * @return response
	 * @throws RetsException if an error occurs
	 */
	public GetObjectResponse<SingleObjectResponse> getObject(String resource, String type, String entity, String id) throws RetsException {
		GetObjectRequest req = new GetObjectRequest(resource, type);
		req.addObject(entity, id);
		return getObject(req);
	}

	/**
	 * Log into the RETS server (see RETS 1.5, section 4).  No other
	 * transactions will work until you have logged in.
	 *
	 * @param userName Username to authenticate
	 * @param password Password to authenticate with
	 * @throws RetsException if authentication was denied
	 */
	public void login(String userName, String password) throws RetsException {
		login(userName, password, null, null);
	}

	/**
	 * Log into the RETS server (see RETS 1.5, section 4).  No other
	 * transactions will work until you have logged in.
	 *
	 * @param userName     username to authenticate
	 * @param password     password to authenticate with
	 * @param brokerCode   broker code if the same user belongs to multiple
	 *                     brokerages.  May be null.
	 * @param brokerBranch branch code if the same user belongs to multiple
	 *                     branches.  May be null.  brokerCode is required if you want
	 *                     brokerBranch to work.
	 * @return LoginResponse if success.
	 * @throws RetsException if authentication was denied
	 */

	public LoginResponse login(String userName, String password, String brokerCode, String brokerBranch) throws RetsException {
		this.httpClient.setUserCredentials(userName, password);

		LoginRequest request = new LoginRequest();
		request.setBrokerCode(brokerCode, brokerBranch);

		LoginResponse response = this.transport.login(request);
		this.capabilityUrls = response.getCapabilityUrls();
		this.transport.setCapabilities(this.capabilityUrls);
		this.setSessionId(response.getSessionId());
		this.getAction();

		return response;
	}

	/**
	 * Log out of the current session.  Another login _may_ re-establish a new connection
	 * depending the the behavior of the {#link RetsHttpClient} and its' ability to
	 * maintain and restablish a connection.
	 *
	 * @throws RetsException if the logout transaction failed
	 */
	public void logout() throws RetsException {
		try {
			this.transport.logout();
		} finally {
			this.setSessionId(null);
		}
	}

	/**
	 * Will perform a search as requested and return a filled
	 * SearchResult object.  This method caches all result information
	 * in memory in the SearchResult object.
	 *
	 * @param req Contains parameters on which to search.
	 * @return a completed SearchResult
	 * @throws RetsException if an error occurs
	 */
	public SearchResult search(SearchRequest req, SAXParserFactory factory) throws RetsException {
		SimpleSearchResult res = new SimpleSearchResult();
		search(req, res, factory);
		return res;
	}

	/**
	 * Execute a RETS Search.  The collector object will be filled
	 * when this method is returned.  See RETS 1.52d, Section 5.
	 *
	 * @param req       Contains parameters on which to search.
	 * @param collector SearchResult object which will be informed of the results
	 *                  as they come in.  If you don't need live results, see the other
	 *                  search invocation.
	 * @throws RetsException if an error occurs
	 */
	public void search(SearchRequest req, SearchResultCollector collector, SAXParserFactory factory) throws RetsException {
		this.transport.search(req, collector, factory);
	}

	/**
	 * Execute a RETS Search. The handler object contains both the collector
	 * and the SAXFactory for parsing the returned XML.
	 *
	 * @param req     the search request
	 * @param handler the search handler
	 */
	public void search(SearchRequest req, SearchResultHandler handler) throws RetsException {
		this.transport.search(req, handler);
	}

	/**
	 * Search and process the Search using a given SearchResultProcessor.
	 *
	 * @param req       the search request
	 * @param processor the result object that will process the data
	 */
	public SearchResultSet search(SearchRequest req, SearchResultProcessor processor) throws RetsException {
		return this.transport.search(req, processor);
	}

	/**
	 * The lowest level integration.  This method is not recommended for general use.
	 */
	public RetsHttpResponse request(RetsHttpRequest request) throws RetsException {
		return this.transport.doRequest(request);
	}

	/**
	 * switch to a specific HttpMethodName, POST/GET, where the
	 * method is supported.  Where GET is not supported, POST
	 * will be used.
	 *
	 * @param method the HttpMethodName to use
	 */
	public void setMethod(String method) {
		this.transport.setMethod(method);
	}

	/**
	 * Performs a search returning only the number of records resulting from a query.
	 * <p>
	 * Convenience method to get number records from a query
	 *
	 * @param req the search request
	 * @return the number of records that returned from the search request
	 * @throws RetsException
	 */
	public int getQueryCount(SearchRequest req, SAXParserFactory factory) throws RetsException {
		req.setCountOnly();
		SearchResult res = this.search(req, factory);
		return res.getCount();
	}

	/**
	 * Gives the URL's of an Object request instead of object themselves
	 * <p>
	 * Convenience method to get the URL's of the requeseted object only
	 *
	 * @param req
	 * @return
	 * @throws RetsException
	 */
	public GetObjectResponse<SingleObjectResponse> getObjectUrl(GetObjectRequest req) throws RetsException {
		req.setLocationOnly(true);
		return this.getObject(req);
	}
}
