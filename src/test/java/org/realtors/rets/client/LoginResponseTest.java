package org.realtors.rets.client;

public class LoginResponseTest extends RetsTestCase {
	public void testValidLoginResponse18() throws RetsException {
		LoginResponse response = new LoginResponse();
		response.parse(getResource("login_valid18.xml"), RetsVersion.v1_8);
		assertEquals("Checking broker code", "B123", response.getBrokerCode());
		assertEquals("Checking broker branch", "BO987", response.getBrokerBranch());
		assertEquals("Checking member name", "Joe T. Schmoe", response.getMemberName());
		assertEquals("Checking metadata version", "1.00.000", response.getMetadataVersion());
		assertEquals("Checking min metadata version", "1.00.000", response.getMinMetadataVersion());
		assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
		assertNull("Checking office list", response.getOfficeList());
		assertEquals("Checking balance", "44.21", response.getBalance());
		assertEquals("Checking timeout", 60, response.getSessionTimeout());
		assertNull("Checking password expiration", response.getPasswordExpiration());
		assertEquals("Checking the vendor name", "CoreLogic", response.getVendorName());
		assertEquals("Checking server product name", "Trestle", response.getServerProductName());
		assertEquals("Checking metadata id", "Trestle_Elastic-12", response.getMetadataId());

		CapabilityUrls urls = response.getCapabilityUrls();
		assertEquals("http://rets.test:6103/get", urls.getActionUrl());
		assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
		assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
		assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
		assertNull(urls.getLoginCompleteUrl());
		assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
		assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
		assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
		assertNull(urls.getUpdateUrl());
	}

	public void testValidLoginResponse17_18() throws RetsException {
		LoginResponse response = new LoginResponse();
		response.parse(getResource("login_valid17_18.xml"), RetsVersion.v1_8);
		assertEquals("Checking broker code", "B123", response.getBrokerCode());
		assertEquals("Checking broker branch", "BO987", response.getBrokerBranch());
		assertEquals("Checking member name", "Jane W. Schmoe", response.getMemberName());
		assertEquals("Checking metadata version", "128.256.512", response.getMetadataVersion());
		assertEquals("Checking min metadata version", "128.256.512", response.getMinMetadataVersion());
		assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
		assertNull("Checking office list", response.getOfficeList());
		assertEquals("Checking balance", "44.21", response.getBalance());
		assertEquals("Checking timeout", 70, response.getSessionTimeout());
		assertNull("Checking password expiration", response.getPasswordExpiration());
		assertEquals("Checking the vendor name", "CoreLogic", response.getVendorName());
		assertEquals("Checking server product name", "Trestle", response.getServerProductName());
		assertEquals("Checking metadata id", "Trestle_Elastic-12", response.getMetadataId());

		CapabilityUrls urls = response.getCapabilityUrls();
		assertEquals("http://rets.test:6103/get", urls.getActionUrl());
		assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
		assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
		assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
		assertNull(urls.getLoginCompleteUrl());
		assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
		assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
		assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
		assertNull(urls.getUpdateUrl());
	}

	public void testValidLoginResponse15() throws RetsException {
		LoginResponse response = new LoginResponse();
		response.parse(getResource("login_valid15.xml"), RetsVersion.v1_8);
		assertEquals("Checking broker", "B123,BO987", response.getBroker());
		assertEquals("Checking member name", "Joe T. Schmoe", response.getMemberName());
		assertEquals("Checking metadata version", "1.00.000", response.getMetadataVersion());
		assertEquals("Checking min metadata version", "1.00.000", response.getMinMetadataVersion());
		assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
		assertNull("Checking office list", response.getOfficeList());
		assertEquals("Checking balance", "44.21", response.getBalance());
		assertEquals("Checking timeout", 60, response.getSessionTimeout());
		assertNull("Checking password expiration", response.getPasswordExpiration());

		CapabilityUrls urls = response.getCapabilityUrls();
		assertEquals("http://rets.test:6103/get", urls.getActionUrl());
		assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
		assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
		assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
		assertNull(urls.getLoginCompleteUrl());
		assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
		assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
		assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
		assertNull(urls.getUpdateUrl());
	}

	public void testValidLoginResponse10() throws RetsException {
		LoginResponse response = new LoginResponse();
		response.parse(getResource("login_valid10.xml"), RetsVersion.v1_0);
		assertEquals("Checking broker", "B123,BO987", response.getBroker());
		assertEquals("Checking member name", "Joe T. Schmoe", response.getMemberName());
		assertEquals("Checking metadata version", "1.00.000", response.getMetadataVersion());
		assertEquals("Checking min metadata version", "1.00.000", response.getMinMetadataVersion());
		assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
		assertNull("Checking office list", response.getOfficeList());
		assertEquals("Checking balance", "44.21", response.getBalance());
		assertEquals("Checking timeout", 60, response.getSessionTimeout());
		assertNull("Checking password expiration", response.getPasswordExpiration());

		CapabilityUrls urls = response.getCapabilityUrls();
		assertEquals("http://rets.test:6103/get", urls.getActionUrl());
		assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
		assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
		assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
		assertNull(urls.getLoginCompleteUrl());
		assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
		assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
		assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
		assertNull(urls.getUpdateUrl());
	}

	public void testLowerCaseKeys() throws RetsException {
		LoginResponse response = new LoginResponse();
		response.parse(getResource("login_lower_case.xml"), RetsVersion.v1_5);
		assertEquals("Checking broker", "B123,BO987", response.getBroker());
		assertEquals("Checking member name", "Joe T. Schmoe", response.getMemberName());
		assertEquals("Checking metadata version", "1.00.000", response.getMetadataVersion());
		assertEquals("Checking min metadata version", "1.00.000", response.getMinMetadataVersion());
		assertEquals("Checking user information", "A123,5678,1,A123", response.getUserInformation());
		assertNull("Checking office list", response.getOfficeList());
		assertEquals("Checking balance", "44.21", response.getBalance());
		assertEquals("Checking timeout", 60, response.getSessionTimeout());
		assertNull("Checking password expiration", response.getPasswordExpiration());

		CapabilityUrls urls = response.getCapabilityUrls();
		assertEquals("http://rets.test:6103/get", urls.getActionUrl());
		assertEquals("http://rets.test:6103/changePassword", urls.getChangePasswordUrl());
		assertEquals("http://rets.test:6103/getObjectEx", urls.getGetObjectUrl());
		assertEquals("http://rets.test:6103/login", urls.getLoginUrl());
		assertNull(urls.getLoginCompleteUrl());
		assertEquals("http://rets.test:6103/logout", urls.getLogoutUrl());
		assertEquals("http://rets.test:6103/search", urls.getSearchUrl());
		assertEquals("http://rets.test:6103/getMetadata", urls.getGetMetadataUrl());
		assertNull(urls.getUpdateUrl());
	}

	public void testStrictLowerCaseKeys() {
		LoginResponse response = new LoginResponse();
		response.setStrict(true);
		try {
			response.parse(getResource("login_lower_case.xml"), RetsVersion.v1_5);
			fail("Should throw exception");
		} catch (RetsException e) {
			// Expected
		}
	}
}
