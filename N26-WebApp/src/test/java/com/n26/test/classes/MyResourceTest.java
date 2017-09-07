package com.n26.test.classes;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import com.n26.test.AddTransactionRequestJSON;
import com.n26.test.MyResource;
import com.n26.test.Statistics;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class MyResourceTest extends JerseyTest {

	Client client = null;

	@Before
	public void init() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		client = Client.create(clientConfig);
	}

	private int addTransactionWithPartitions(Double amount, int lagFromNow) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.add(Calendar.SECOND, lagFromNow * -1);

		AddTransactionRequestJSON addTransactionRequestJSON = new AddTransactionRequestJSON();
		addTransactionRequestJSON.setAmount(amount);
		addTransactionRequestJSON.setTimeStamp(c.getTimeInMillis());

		String postURL = "http://localhost:9998";
		WebResource webResourcePost = client.resource(postURL);
		ClientResponse response = webResourcePost.path("api/transactions").type("application/json")
				.post(ClientResponse.class, addTransactionRequestJSON);
		int result = response.getStatus();

		return result;
	}

	private int addTransaction(Double amount, int lagFromNow) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.add(Calendar.SECOND, lagFromNow * -1);

		AddTransactionRequestJSON addTransactionRequestJSON = new AddTransactionRequestJSON();
		addTransactionRequestJSON.setAmount(amount);
		addTransactionRequestJSON.setTimeStamp(c.getTimeInMillis());

		String postURL = "http://localhost:9998";
		WebResource webResourcePost = client.resource(postURL);
		ClientResponse response = webResourcePost.path("api/transactions/normal").type("application/json")
				.post(ClientResponse.class, addTransactionRequestJSON);
		int result = response.getStatus();

		return result;
	}

	private String readStatisticsWithPartitions() {
		// GET request to findBook resource with a query parameter
		String getBookURL = "http://localhost:9998";
		WebResource webResourceGet = client.resource(getBookURL);
		ClientResponse response = webResourceGet.path("api/statistics").get(ClientResponse.class);
		Statistics responseEntity = response.getEntity(Statistics.class);

		if (response.getStatus() != 200) {
			throw new WebApplicationException();
		}

		return responseEntity.toString();
	}

	private String readStatistics() {
		// GET request to findBook resource with a query parameter
		String getBookURL = "http://localhost:9998";
		WebResource webResourceGet = client.resource(getBookURL);
		ClientResponse response = webResourceGet.path("api/statistics/normal").get(ClientResponse.class);
		Statistics responseEntity = response.getEntity(Statistics.class);

		if (response.getStatus() != 200) {
			throw new WebApplicationException();
		}

		return responseEntity.toString();
	}

	private void waitAminuite() {
		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void clearList() {

		String postURL = "http://localhost:9998";
		WebResource webResourcePost = client.resource(postURL);
		ClientResponse response = webResourcePost.path("api/clear").delete(ClientResponse.class);
		String result = response.getEntity(String.class);

		assertEquals("success", result);
	}

	/*
	 * @Test public void shouldinsertTransactionsInPartitionsAndReadStats() {
	 * List<Double> doubles = new ArrayList<>(); doubles.add(15d);
	 * doubles.add(45d); doubles.add(30d); doubles.add(30d); clearList(); int
	 * lag = 55; for (int i = 0; i < doubles.size(); i++) {
	 * addTransactionWithPartitions(doubles.get(i), lag - i); }
	 * 
	 * // addTransaction(700d,0);
	 * 
	 * String statatisticsResult = readStatisticsWithPartitions();
	 * 
	 * assertEquals(
	 * "Statistics [sum=120.0, avg=30.0, max=45.0, min=15.0, count=4]",
	 * statatisticsResult);
	 * 
	 * }
	 */
	
	@Test
	public void shouldinsertTransactionsInListAndReadStats204() {
		List<Double> doubles = new ArrayList<>();
		doubles.add(15d);
		doubles.add(45d);
		doubles.add(30d);
		doubles.add(30d);
		clearList();

		for (int i = 0; i < doubles.size(); i++) {
			int result = addTransaction(doubles.get(i), 90);
			assertEquals(204, result);
		}

		

		String statatisticsResult = readStatistics();

		assertEquals("Statistics [sum=0.0, avg=0.0, max=0.0, min=0.0, count=0]", statatisticsResult);

	}
	
	@Test
	public void shouldinsertTransactionsInListAndReadStats201() {
		List<Double> doubles = new ArrayList<>();
		doubles.add(15d);
		doubles.add(45d);
		doubles.add(30d);
		doubles.add(30d);
		clearList();

		for (int i = 0; i < doubles.size(); i++) {
			int result = addTransaction(doubles.get(i), 10);
			assertEquals(201, result);
		}

		

		String statatisticsResult = readStatistics();

		assertEquals("Statistics [sum=120.0, avg=30.0, max=45.0, min=15.0, count=4]", statatisticsResult);

	}

	@Override
	protected Application configure() {
		return new ResourceConfig(MyResource.class);
	}

}
