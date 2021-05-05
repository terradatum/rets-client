package org.realtors.rets.examples;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.client.*;

import javax.xml.parsers.SAXParserFactory;
import java.util.Collections;

/**
 * Simple Example performing a search and iterating over the results
 *
 */
public class RetsSearchExample {

	public static void main(String[] args) {

		//Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
		RetsHttpClient httpClient = new CommonsHttpClient();
		RetsVersion retsVersion = RetsVersion.v1_7_2;
		String loginUrl = "http://theurloftheretsserver.com";

		//Create a RetesSession with RetsHttpClient
		RetsSession session = new RetsSession(loginUrl, httpClient, retsVersion);    

		String username = "username";
		String password = "password";

		//Set method as GET or POST
		session.setMethod("POST");
		try {
			//Login
			session.login(username, password);
		} catch (RetsException e) {
			e.printStackTrace();
		}

		String sQuery = "(Member_num=.ANY.)";
		String sResource = "Property";
		String sClass = "Residential";

		//Create a SearchRequest
		SearchRequest request = new SearchRequest(sResource, sClass, sQuery);

		//Select only available fields
		String select ="field1,field2,field3,field4,field5";
		request.setSelect(select);

		//Set request to retrieve count if desired
		request.setCountFirst();

		try {
			//Execute the search
			SimpleSearchResult searchResult = new SimpleSearchResult();
			//Ignore RETS Reply Code 20203 - often, the server will still return data
			SearchResultHandler handler = new SearchResultHandler(
					searchResult,
					new IgnoreInvalidReplyCodeHandler(Collections.singletonList(20203)),
					CompactRowPolicy.DEFAULT);
			session.search(request, handler);

			//Print out count and columns
			int count = searchResult.getCount();
			System.out.println("COUNT: " + count);
			System.out.println("COLUMNS: " + StringUtils.join(searchResult.getColumns(), "\t"));

			//Iterate over, print records
			for (int row = 0; row < searchResult.getRowCount(); row++){
				System.out.println("ROW"+ row +": " + StringUtils.join(searchResult.getRow(row), "\t"));
			}
		} catch (RetsException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				session.logout();
			}
			catch(RetsException e) {
				// this happens a lot, usually "Invalid RETS Session ID" - best to trace the exception but ignore it
				e.printStackTrace();
			}
		}
	}
}
