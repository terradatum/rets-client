# rets-client

The Terradatum RETS Client is a library used to access data on RETS compliant servers. This is a fork with the following
ancestry:

* [LucidSoftware][lucid-software]
* [Trulia Java Rets Client][trulia-java-rets-client]
* [jrets][jrets]
* [CART RETS client][cart-rets-client]

This project no longer builds a "fat" Jar. For our purposes, the code is best served as a library Jar, which is then
consumed by an Executable Jar, whether that consuming Jar is a single-process executable or a distributed microservice
running in k8s.

This build is a major breaking-change if you consume Metadata - mostly a name change spree that was totally unnecessary, but made me happy. Something like `MClass` -> `RetsClass`.

The [`CommonsHttpClient`][commons-http-client] code is based on the deprecated
Http Client from Apache Commons, and needs to be migrated to [`HttpClient 5.1`][apache-http-client-5].

Simple example of a search:

```java
public class RetsSearchExample {

	public static void main(String[] args) throws MalformedURLException {

		//Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
		RetsHttpClient httpClient = new CommonsHttpClient();
		RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
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
		String select = "field1,field2,field3,field4,field5";
		request.setSelect(select);

		//Set request to retrive count if desired
		request.setCountFirst();

		SearchResultImpl response;
		try {
			//Execute the search
			response = (SearchResultImpl) session.search(request);

			//Print out count and columns
			int count = response.getCount();
			System.out.println("COUNT: " + count);
			System.out.println("COLUMNS: " + StringUtils.join(response.getColumns(), "\t"));

			//Iterate over, print records
			for (int row = 0; row < response.getRowCount(); row++) {
				System.out.println("ROW" + row + ": " + StringUtils.join(response.getRow(row), "\t"));
			}
		} catch (RetsException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.logout();
				} catch (RetsException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
```

Simple example making a GetObjectRequest:

```java
public class RetsGetObjectExample {

	public static void main(String[] args) throws MalformedURLException {

		//Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
		RetsHttpClient httpClient = new CommonsHttpClient();
		RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
		String loginUrl = "http://theurloftheretsserver.com";

		//Create a RetesSession with RetsHttpClient
		RetsSession session = new RetsSession(loginUrl, httpClient, retsVersion);

		String username = "username";
		String password = "password";
		try {
			//Login
			session.login(username, password);
		} catch (RetsException e) {
			e.printStackTrace();
		}

		String sResource = "Property";
		String objType = "Photo";
		String seqNum = "*"; // * denotes get all pictures associated with id (from Rets Spec)
		List<String> idsList = Arrays.asList("331988", "152305", "243374");
		try {
			//Create a GetObjectRequeset
			GetObjectRequest req = new GetObjectRequest(sResource, objType);

			//Add the list of ids to request on (ids can be determined from records)
			Iterator<String> idsIter = idsList.iterator();
			while (idsIter.hasNext()) {
				req.addObject(idsIter.next(), seqNum);
			}

			//Execute the retrieval of objects 
			Iterator<SingleObjectResponse> singleObjectResponseIter = session.getObject(req).iterator();

			//Iterate over each Object 
			while (singleObjectResponseIter.hasNext()) {
				SingleObjectResponse sor = (SingleObjectResponse) singleObjectResponseIter.next();

				//Retrieve in info and print
				String type = sor.getType();
				String contentID = sor.getContentID();
				String objectID = sor.getObjectID();
				String description = sor.getDescription();
				String location = sor.getLocation();
				InputStream is = sor.getInputStream();

				System.out.print("type:" + type);
				System.out.print(" ,contentID:" + contentID);
				System.out.print(" ,objectID:" + objectID);
				System.out.println(" ,description:" + description);
				System.out.println("location:" + location);

				//Download object
				try {
					String dest = "/path/of/dowload/loaction";
					int size = is.available();
					String filename = dest + contentID + "-" + objectID + ".jpeg";
					OutputStream out = new FileOutputStream(new File(filename));
					int read;
					byte[] bytes = new byte[1024];

					while ((read = is.read(bytes)) != -1) {

						out.write(bytes, 0, read);
					}

					is.close();
					out.flush();
					out.close();

					System.out.println("New file with size " + size + " created: " + filename);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}

			}

		} catch (RetsException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.logout();
				} catch (RetsException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
```

Example of Geting Metadata:

```java
public class RetsGetMetadataExample {

	public static void main(String[] args) throws MalformedURLException {

		//Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
		RetsHttpClient httpClient = new CommonsHttpClient();
		RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
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
			MSystem system = session.getMetadata().getSystem();
			System.out.println(
					"SYSTEM: " + system.getSystemID() +
							" - " + system.getSystemDescription());

			for (MResource resource : system.getMResources()) {

				System.out.println(
						"    RESOURCE: " + resource.getResourceID());

				for (MClass classification : resource.getMClasses()) {
					System.out.println(
							"        CLASS: " + classification.getClassName() +
									" - " + classification.getDescription());
				}
			}
		} catch (RetsException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.logout();
				} catch (RetsException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
```

## License

[Terradatum RETS Client is licensed under the MIT License](https://github.com/terradatum/rets-client/blob/master/LICENSE)

[lucid-software]: https://github.com/lucidsoftware/trulia-java-rets-client

[trulia-java-rets-client]: https://github.com/trulia/trulia-java-rets-client

[jrets]: https://github.com/jpfielding/jrets

[cart-rets-client]: http://cart.sourceforge.net/

[commonshttpclient]: src/main/java/org/realtors/rets/client/CommonsHttpClient.java

[apache-http-client-5]: https://hc.apache.org/httpcomponents-client-5.1.x/index.html
