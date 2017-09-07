package com.n26.test.main;

import java.util.Calendar;
import java.util.TimeZone;

import javax.validation.constraints.AssertTrue;
import javax.ws.rs.WebApplicationException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.client.ClientConfig;

import com.n26.test.AddTransactionRequestJSON;
import com.n26.test.Statistics;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * This class launches the web application in an embedded Jetty container. This is the entry point to your application. The Java
 * command that is used for launching should fire this main method.
 */
public class Main {

    /*public static void main(String[] args) throws Exception{
        // The port that we should run on can be set into an environment variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        final Server server = new Server(Integer.valueOf(webPort));
        final WebAppContext root = new WebAppContext();

        root.setContextPath("/");
        // Parent loader priority is a class loader setting that Jetty accepts.
        // By default Jetty will behave like most web containers in that it will
        // allow your application to replace non-server libraries that are part of the
        // container. Setting parent loader priority to true changes this behavior.
        Calendar c = Calendar.getInstance();
        
        System.out.println(c.getTimeInMillis());
        c.add(Calendar.SECOND, -30);
        System.out.println(c.getTimeInMillis());
        c.add(Calendar.SECOND, -70);
        System.out.println(c.getTimeInMillis());
        
        
        
        root.setParentLoaderPriority(true);

        final String webappDirLocation = "src/main/webapp/";
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);

        server.setHandler(root);

        server.start();
        //server.join();
    }*/
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(c.getTimeInMillis());
        System.out.println((c.getTimeInMillis()/1000)*1000);
        c.add(Calendar.SECOND, -3);
        System.out.println(c.getTimeInMillis());
        c.add(Calendar.SECOND, -7);
        System.out.println(c.getTimeInMillis());
	}
	
	
	/*public static void main(String[] args) {

        // Create Jersey client
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);

        
        
        
        
        
        
        
        
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.add(Calendar.SECOND, -1);
        System.out.println(c.getTimeInMillis());
        AddTransactionRequestJSON addTransactionRequestJSON = new AddTransactionRequestJSON();
        addTransactionRequestJSON.setAmount(104d);
        addTransactionRequestJSON.setTimeStamp(c.getTimeInMillis());
        
        String postURL = "http://localhost:8080/N26-WebApp/api/transactions";
        WebResource webResourcePost = client.resource(postURL);
        ClientResponse response = webResourcePost.type("application/json").post(ClientResponse.class, addTransactionRequestJSON);
        String result = response.getEntity(String.class);

        System.out.println(result.toString());
        
        
        
     // GET request to findBook resource with a query parameter
        String getBookURL = "http://localhost:8080/N26-WebApp/api/statistics";
        WebResource webResourceGet = client.resource(getBookURL);
         response = webResourceGet.get(ClientResponse.class);
        Statistics responseEntity = response.getEntity(Statistics.class);

        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }

        System.out.println(responseEntity.toString());
        
        
    }
*/
}
