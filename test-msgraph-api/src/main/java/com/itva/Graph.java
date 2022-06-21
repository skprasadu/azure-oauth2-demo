package com.itva;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DeviceCodeInfo;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.ListItem;
import com.microsoft.graph.models.Site;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.ListCollectionPage;
import com.microsoft.graph.requests.ListItemCollectionPage;
import com.microsoft.graph.requests.SiteCollectionPage;

import okhttp3.Request;

public class Graph {
	private static Properties _properties;

	public static void initializeGraphForUserAuth(Properties properties, Consumer<DeviceCodeInfo> challenge) throws Exception {
	    // Ensure properties isn't null
	    if (properties == null) {
	        throw new Exception("Properties cannot be null");
	    }

	    _properties = properties;
	}
	
	private static ClientSecretCredential _clientSecretCredential;
	private static GraphServiceClient<Request> _appClient;
	private static String _token;
	
	private static void ensureGraphForAppOnlyAuth() throws Exception {
	    // Ensure _properties isn't null
	    if (_properties == null) {
	        throw new Exception("Properties cannot be null");
	    }

	    if (_clientSecretCredential == null) {
	        final String clientId = _properties.getProperty("app.clientId");
	        final String tenantId = _properties.getProperty("app.tenantId");
	        final String clientSecret = _properties.getProperty("app.clientSecret");

	        _clientSecretCredential = new ClientSecretCredentialBuilder()
	            .clientId(clientId)
	            .tenantId(tenantId)
	            .clientSecret(clientSecret)
	            .build();
	    }

	    if (_appClient == null) {
	    	List<String> list = new ArrayList<>();
	    	list.add("https://graph.microsoft.com/.default");
	    	
	        final TokenCredentialAuthProvider authProvider =
	            new TokenCredentialAuthProvider(
	                list, _clientSecretCredential);
	        
	        _token = authProvider.getAuthorizationTokenAsync(new URL("https://graph.microsoft.com/v1.0/me")).get();
	        
	        System.out.println("token=" + _token);
	        
	        getData();

	        _appClient = GraphServiceClient.builder()
	            .authenticationProvider(authProvider)
	            .buildClient();
	    }	    
	}
	
	private static void getData() {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+ _token);
		headers.set("Connection", "keep-alive");
		headers.set("Content-type", "application/json");
		
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/drives/b!TuQfhSf0eES78hdFH0_ngn3pm3meigpCqYW0YfQ7tIIXqrspq7mVRqeUpnGSGJSg/items/01BXWRSJOMNAJVMFGMYZHZIK5YYQKENRGG/analytics/allTime", 
			HttpMethod.GET, entity, String.class, "$expand=activities($filter=access ne null)");

		System.out.println(response.getBody());
	}
	

	public static SiteCollectionPage getUsers() throws Exception {
	    ensureGraphForAppOnlyAuth();
	    
	    LinkedList<Option> requestOptions = new LinkedList<Option>();
	    requestOptions.add(new QueryOption("search", "kp-team"));
	    
	    SiteCollectionPage permissions = _appClient.sites()
	    		.buildRequest(requestOptions)
	    		.get();
	    
	    System.out.println(permissions );

	    return permissions;
	}

	public static void getUsersHelper() {
		System.out.println("Java Graph Tutorial");
	    System.out.println();

	    final Properties oAuthProperties = new Properties();
	    try {
	        oAuthProperties.load(App.class.getResourceAsStream("oAuth.properties"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    System.out.println(oAuthProperties);
	    initializeGraph(oAuthProperties);

	    listUsers();
	}
	
	private static void initializeGraph(Properties properties) {
		try {
	        Graph.initializeGraphForUserAuth(properties,
	            challenge -> System.out.println(challenge.getMessage()));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private static void listUsers() {
	    try {
	        final SiteCollectionPage users = Graph.getUsers();

	        // Output each user's details
	        for (Site user: users.getCurrentPage()) {
	            System.out.println("Site displayName: " + user.displayName);
	            System.out.println("  ID: " + user.id);
	            System.out.println("  analytics: " + user.analytics);
	        }
	        
	        ListCollectionPage ds =_appClient.sites(users.getCurrentPage().get(0).id)
	        	.lists()
    			.buildRequest()
    			.get();
	        
	        for (com.microsoft.graph.models.List ls: ds.getCurrentPage()) {
	            System.out.println("List displayName: " + ls.name);
	            System.out.println("  ID: " + ls.id);
	            
	            if(ls.name.equals("Shared Documents")) {
	    	        ListItemCollectionPage ds1 =_appClient.sites(users.getCurrentPage().get(0).id)
	    		        	.lists(ls.id)
	    		        	.items()
	    	    			.buildRequest()
	    	    			.get();
	            	
	    	        for (ListItem ls1: ds1.getCurrentPage()) {
	    	        	System.out.println("  ID: " + ls1.id);
	    	        	System.out.println("  ID: " + ls1.webUrl);
	    	        	
	    	        	LinkedList<Option> requestOptions = new LinkedList<Option>();
	    	    	    requestOptions.add(new QueryOption("$expand", "listItem"));
	    	        	
	    	        	DriveItem ds2 =_appClient.sites(users.getCurrentPage().get(0).id)
		    		        	.lists(ls.id)
		    		        	.items(ls1.id)
		    		        	.driveItem()
		    	    			.buildRequest(requestOptions)
		    	    			.get();
	    	        	
	    	        	System.out.println("  ID: " + ds2.id);
	    	        	System.out.println(" Drive ID: " + ds2.parentReference.driveId);
	    	        	//driveItem?$expand=listItem
	    	        	
	    	        	//https://graph.microsoft.com/v1.0/drives/b!TuQfhSf0eES78hdFH0_ngn3pm3meigpCqYW0YfQ7tIIXqrspq7mVRqeUpnGSGJSg/items/01BXWRSJK2CCP6RNAAFRHLEZ6RW6QEDS6Z/
	    	    	    //analytics/allTime?$expand=activities($filter=access ne null)

	    	    	    /*ItemAnalytics ia =_appClient.drives(ds2.parentReference.driveId)
		    		        	.items(ds2.id)
		    		        	.analytics()
		    	    			.buildRequest()
		    	    			.get();
	    	        	
	    	    	    for(ItemActivityStat k: ia.itemActivityStats.getCurrentPage()) {
	    	    	    	System.out.println("Action Count " + k.access.actionCount);
	    	    	    	System.out.println("Actor Count " + k.access.actorCount);
	    	    	    	if(k.activities != null) {
		    	    	    	for(ItemActivity k1: k.activities.getCurrentPage()) {
			    	    	    	System.out.println("Actor Name " + k1.actor.user);	    	    	    	    
		    	    	    	}
	    	    	    	}
	    	    	    }
	    	        	
	    	    	    ActivityStatisticsCollectionPage activityStatistics = _appClient.me().analytics().activityStatistics()
		    	    	    .buildRequest()
		    	    		.get();*/
	    	    	    
	    	    	    
	    	        }
	            }
	        }	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}
