package org.realtors.rets.examples;

import org.realtors.rets.client.CommonsHttpClient;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.client.RetsHttpClient;
import org.realtors.rets.client.RetsSession;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.metadata.types.RetsClass;
import org.realtors.rets.metadata.types.RetsResource;
import org.realtors.rets.metadata.types.RetsSystem;

/**
 * Simple Example performing a GetMetadata and iterating of the results
 *
 */
public class RetsGetMetadataExample {

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

		try {
			RetsSystem system = session.getMetadata().getSystem();
			System.out.println(
					"SYSTEM: " + system.getSystemID() + 
					" - " + system.getSystemDescription());

			for(RetsResource resource: system.getRetsResources()) {

				System.out.println(
						"    RESOURCE: " + resource.getResourceID());

				for(RetsClass classification: resource.getRetsClasses()) {
					System.out.println(
							"        CLASS: " + classification.getClassName() +
							" - " + classification.getDescription());
				}
			}
		}
		catch (RetsException e) {
			e.printStackTrace();
		} 	
		finally {
			if(session != null) { 
				try {
					session.logout(); 
				} 
				catch(RetsException e) {
					e.printStackTrace();
				}
			}
		}
	}	
}
