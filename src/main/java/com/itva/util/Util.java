package com.itva.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.itva.model.TitusAttribute;
import com.itva.model.TitusAttributes;
import com.itva.model.TitusDocument;
import com.itva.model.activitiespayload.VisitorsPayload;
import com.itva.model.filecollection.FileCollection;
import com.itva.model.filecollection.Value;
import com.itva.model.itemiddetails.ItemIdDetails;

public class Util {
	private static Logger logger = LoggerFactory.getLogger(Util.class);

	public static TitusDocument convertToTitusDocument(TitusAttributes titusAttributes, List<TitusAttribute> tas) {
		Timestamp ts = new Timestamp(Calendar.getInstance().getTimeInMillis());
		TitusDocument td = TitusDocument.builder().documentName(titusAttributes.getDocumentName())
				.userName(titusAttributes.getUserName()).accessType(titusAttributes.getAccessType()).loggedTime(ts)
				.build();

		Field[] allFields = TitusAttributes.class.getDeclaredFields();

		for (Field f : allFields) {

			try {
				String propertyName = f.getName();
				String methodName = "get" + propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1, propertyName.length());
				try {
					Method method = TitusAttributes.class.getMethod(methodName, null);
					String value = (String) method.invoke(titusAttributes, null);
					logger.debug(f.getName() + " " + value);
					if (propertyName.equals("documentName") || propertyName.equals("userName")
							|| propertyName.equals("accessType")) {
						continue;
					}
					tas.add(TitusAttribute.builder().documentName(titusAttributes.getDocumentName())
							.attributeName(f.getName()).attributeValue(value).loggedTime(ts).build());
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return td;
	}

	public static List<TitusAttributes> convertToTitusAttributes(List<TitusDocument> tds, List<TitusAttribute> taList) {
		List<TitusAttributes> tasList = new ArrayList<>();

		for (TitusDocument td : tds) {
			TitusAttributes tas = TitusAttributes.builder().documentName(td.getDocumentName())
					.userName(td.getUserName()).accessType(td.getAccessType()).loggedTime(td.getLoggedTime()).build();

			List<TitusAttribute> taListByDoc = taList.stream()
					.filter(x -> x.getDocumentName().equals(td.getDocumentName())).collect(Collectors.toList());

			for (TitusAttribute ta : taListByDoc) {
				String propertyName = ta.getAttributeName();
				String methodName = "set" + propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1, propertyName.length());

				try {
					Method method = TitusAttributes.class.getMethod(methodName, String.class);
					method.invoke(tas, ta.getAttributeValue());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			tasList.add(tas);
		}

		return tasList;
	}
	
	public static VisitorsPayload getItemIdDetails(String fileName, String token) {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+ token);
		headers.set("Connection", "keep-alive");
		headers.set("Content-type", "application/json");
		
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String url1 = "https://graph.microsoft.com/v1.0/sites/tradecollaborationengine.sharepoint.com,851fe44e-f427-4478-bbf2-17451f4fe782,799be97d-8a9e-420a-a985-b461f43bb482/lists/29bbaa17-b9ab-4695-a794-a671921894a0/items";
		ResponseEntity<String> response = restTemplate.exchange(url1, HttpMethod.GET, entity, String.class);

		System.out.println(response.getBody());
		FileCollection fileCollection = new Gson().fromJson(response.getBody(), FileCollection.class);
		
		Integer id = -1;
		for(Value value :fileCollection.getValue()) {
			if(value.getWebUrl().endsWith(fileName)) {
				id = value.getId();
				break;
			}
		}
		
		if(id != -1) {
			String url2 = String.format("https://graph.microsoft.com/v1.0/sites/tradecollaborationengine.sharepoint.com,851fe44e-f427-4478-bbf2-17451f4fe782,799be97d-8a9e-420a-a985-b461f43bb482/lists/29bbaa17-b9ab-4695-a794-a671921894a0/items/%d/driveItem?$expand=listItem", id);
				
			ResponseEntity<String> response1 = restTemplate.exchange(url2, HttpMethod.GET, entity, String.class);

			System.out.println(response1.getBody());
			
			ItemIdDetails itemIdDetails = new Gson().fromJson(response1.getBody(), ItemIdDetails.class);
			
			String url3 = String.format("https://graph.microsoft.com/v1.0/drives/b!TuQfhSf0eES78hdFH0_ngn3pm3meigpCqYW0YfQ7tIIXqrspq7mVRqeUpnGSGJSg/items/%s/analytics/allTime?$expand=activities($filter=access ne null)", itemIdDetails.getId());
			ResponseEntity<String> response2 = restTemplate.exchange(url3, HttpMethod.GET, entity, String.class);

			System.out.println(response1.getBody());
			
			VisitorsPayload visitorsPayload = new Gson().fromJson(response2.getBody(), VisitorsPayload.class);
			return visitorsPayload;
		}
		return null;
	}
	
}
