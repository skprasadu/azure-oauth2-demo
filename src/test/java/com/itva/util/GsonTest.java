package com.itva.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import com.google.gson.Gson;
import com.itva.model.activitiespayload.VisitorsPayload;
import com.itva.model.filecollection.FileCollection;
import com.itva.model.itemiddetails.ItemIdDetails;

public class GsonTest {
	
	@Test
	public void testActivitiesJson() {
		try (Reader reader = new InputStreamReader(this.getClass()
	            .getResourceAsStream("/activities.json"))) {
			VisitorsPayload result = new Gson().fromJson(reader, VisitorsPayload.class);
	        System.out.println(result.isTrending());  // prints "bat"
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}
	
	@Test
	public void testFileCollectionJson() {
		try (Reader reader = new InputStreamReader(this.getClass()
	            .getResourceAsStream("/fileCollection.json"))) {
			FileCollection result = new Gson().fromJson(reader, FileCollection.class);
	        System.out.println(result.getValue().get(0).getWebUrl());  // prints "bat"
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}
	
	@Test
	public void testItemiddetailsJson() {
		try (Reader reader = new InputStreamReader(this.getClass()
	            .getResourceAsStream("/itemiddetails.json"))) {
			ItemIdDetails result = new Gson().fromJson(reader, ItemIdDetails.class);
	        System.out.println(result.getId());  // prints "bat"
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}


}
