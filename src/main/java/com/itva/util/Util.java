package com.itva.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.itva.model.AuditLog;
import com.itva.model.TitusAttribute;
import com.itva.model.TitusAttributes;
import com.itva.model.TitusDocument;
import com.itva.model.TitusDocument2;
import com.itva.model.ViewRecord;
import com.itva.model.activitiespayload.VisitorsPayload;
import com.itva.model.filecollection.FileCollection;
import com.itva.model.filecollection.Value;
import com.itva.model.itemiddetails.ItemIdDetails;
import com.itva.repositories.TitusDocument2Repository;

import lombok.val;

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
					Class<String> cls = String.class;
					/*
					 * if(methodName.equals("setLoggedTime")) { cls = Timestamp.class; }
					 */
					Method method = TitusAttributes.class.getMethod(methodName, cls);
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

	public static ArrayList<VisitorsPayload> getItemIdDetails(String tcSiteId, String tcSharedDocumentListId,
			String tcSharedDocumentDriveId, String token) {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		headers.set("Connection", "keep-alive");
		headers.set("Content-type", "application/json");

		val entity = new HttpEntity<String>(headers);

		String url1 = String.format("https://graph.microsoft.com/v1.0/sites/%s/lists/%s/items?expand=fields", tcSiteId,
				tcSharedDocumentListId);
		val response = restTemplate.exchange(url1, HttpMethod.GET, entity, String.class);

		// System.out.println(response.getBody());
		FileCollection fileCollection = new Gson().fromJson(response.getBody(), FileCollection.class);

		val visitorsPayloadList = new ArrayList<VisitorsPayload>();
		for (Value value : fileCollection.getValue()) {
			// System.out.println("map=" + value.getFields());
			String url2 = String.format(
					"https://graph.microsoft.com/v1.0/sites/%s/lists/%s/items/%d/driveItem?$expand=listItem", tcSiteId,
					tcSharedDocumentListId, value.getId());

			ResponseEntity<String> response1 = restTemplate.exchange(url2, HttpMethod.GET, entity, String.class);

			// System.out.println(response1.getBody());

			ItemIdDetails itemIdDetails = new Gson().fromJson(response1.getBody(), ItemIdDetails.class);

			String url3 = String.format(
					"https://graph.microsoft.com/v1.0/drives/%s/items/%s/analytics/allTime?$expand=activities($filter=access ne null)",
					tcSharedDocumentDriveId, itemIdDetails.getId());
			ResponseEntity<String> response2 = restTemplate.exchange(url3, HttpMethod.GET, entity, String.class);

			// System.out.println(response1.getBody());

			VisitorsPayload visitorsPayload = new Gson().fromJson(response2.getBody(), VisitorsPayload.class);

			String[] split = value.getWebUrl().split("/");
			visitorsPayload.setFileName(split[split.length - 1]);
			visitorsPayload.setFields(value.getFields());
			visitorsPayloadList.add(visitorsPayload);
		}
		return visitorsPayloadList;
	}

	public static void checkNewDownloads(JdbcTemplate jdbcTemplate, List<TitusDocument2> tds, String siteUrl,
			TitusDocument2Repository titusDocument2Repository) {
		val sql = "SELECT * FROM AuditLog where siteUrl=?";

		val list = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AuditLog.class), siteUrl);

		val currentDownLoadSet = tds
				.stream().filter(x -> x.getAccessType().equals("FileDownloaded")).map(x -> ViewRecord.builder()
						.fileName(x.getDocumentName()).activityDateTime(x.getLoggedTime()).email(x.getUserId()).build())
				.collect(Collectors.toSet());

		val newDownLoadSet = list
				.stream().map(x -> ViewRecord.builder().fileName(x.getSourceFileName())
						.activityDateTime(x.getCreationTime()).email(x.getUserId()).build())
				.collect(Collectors.toSet());

		Predicate<ViewRecord> notInDatabase = x -> !currentDownLoadSet.contains(x);

		BiFunction<TitusDocument2, ViewRecord, Boolean> fileAndUser = (x,
				vr) -> x.getDocumentName().equals(vr.getFileName()) && x.getUserId() != null
						&& x.getUserId().equals(vr.getEmail());

		val tdList = newDownLoadSet.stream().filter(notInDatabase).map(vr -> tds.stream()
				.filter(x -> fileAndUser.apply(x, vr)).findAny()
				.map(x -> TitusDocument2.builder().documentName(vr.getFileName()).userId(vr.getEmail())
						.userName(x.getUserName()).accessType("FileDownloaded").eci(x.getEci()).eciCoC(x.getEciCoC())
						.eciJuris(x.getEciJuris()).eciClass(x.getEciClass()).export(x.getExport()).exAuth(x.getExAuth())
						.containsCUI(x.getContainsCUI()).cui(x.getCui()).dissemination(x.getDissemination())
						.proprietary(x.getProprietary()).proprietaryType(x.getProprietaryType())
						.proprietaryStatement(x.getProprietaryStatement()).loggedTime(vr.getActivityDateTime())
						.siteFullPath(siteUrl).build())
				.orElseGet(() -> TitusDocument2.builder().documentName(vr.getFileName()).userId(vr.getEmail())
						.accessType("FileDownloaded").loggedTime(vr.getActivityDateTime()).build()))
				.collect(Collectors.toList());

		// System.out.println("tdList= " + tdList);
		tdList.forEach(titusDocument2Repository::save);
	}
}
