package com.itva.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itva.model.TitusDocument2;
import com.itva.model.ViewRecord;
import com.itva.model.activitiespayload.Activities;
import com.itva.repositories.TitusDocument2Repository;
import com.itva.util.Util;

import lombok.val;

@RestController
@RequestMapping("/api")
public class TitusAuditLogController2 {
	private Logger logger = LoggerFactory.getLogger(TitusAuditLogController2.class);

	@Autowired
	private TitusDocument2Repository titusDocument2Repository;

	@Autowired
	private OAuth2AuthorizedClientService clientService;
	
	@Value("${tc.site-id}")
	private String tcSiteId;

	@Value("${tc.shared-document-list-id}")
	private String tcSharedDocumentListId;
	
	@Value("${tc.shared-document-drive-id}")
	private String tcSharedDocumentDriveId;

	@GetMapping("listTitusAuditLogs2")
	public List<TitusDocument2> listTitusAuditLogs(@RequestParam("checkForChanges") Boolean checkForChanges) {		
		logger.debug("******* In listTitusAuditLogs");
		
		if(checkForChanges) {
			val tds = titusDocument2Repository.findAll();
		
			checkForNewViewsAndInsert(tds);
		}
		
		val tds = titusDocument2Repository.findAll();

		Collections.sort(tds, (s1, s2) -> s2.getLoggedTime().compareTo(s1.getLoggedTime()) );

		return tds;
	}

	private void checkForNewViewsAndInsert(List<TitusDocument2> list) {
		val newViewedSet = new HashSet<ViewRecord>();
		String token = getToken();
		
		val visitorsPayloadList = Util.getItemIdDetails(tcSiteId, tcSharedDocumentListId, tcSharedDocumentDriveId, token);
		
		for(val visitorsPayload: visitorsPayloadList) {
			if(visitorsPayload.getActivities() != null) {
				for(Activities activities : visitorsPayload.getActivities()) {
					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
					LocalDateTime localdatetime = LocalDateTime.parse(activities.getActivityDateTime(), format);
					Timestamp ts = Timestamp.valueOf(localdatetime);
					newViewedSet.add(new ViewRecord(visitorsPayload.getFileName(), ts, activities.getActor().getUser().getDisplayName(), visitorsPayload.getFields()));
				}
			}
		}
		System.out.println("newViewedSet=" + newViewedSet);
		logger.debug("********************");
		
		val readList = list.stream().filter(x -> x.getAccessType().equals("READ")).collect(Collectors.toList());
		val existingViewedSet = new HashSet<ViewRecord>();
		
		for(TitusDocument2 t: readList) {
			existingViewedSet.add(new ViewRecord( t.getDocumentName(), t.getLoggedTime(), t.getUserName(), null));
		}
				
		logger.debug("********************");
		logger.debug("existingViewedSet=" + existingViewedSet);
		
		for(ViewRecord vr: newViewedSet) {
			logger.debug("VR=" + vr);
			if(!existingViewedSet.contains(vr)) {
				titusDocument2Repository.save(TitusDocument2.builder()
						.documentName(vr.getFileName())
						.userName(vr.getDisplayName())
						.accessType("READ")
						.loggedTime( vr.getActivityDateTime()).build());
			}
		}
	}

	private String getToken() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) securityContext.getAuthentication();

		OAuth2AuthorizedClient client = clientService
				.loadAuthorizedClient(oauth2Token.getAuthorizedClientRegistrationId(), oauth2Token.getName());

		//System.out.println("token=" + client.getAccessToken().getTokenValue());
		return client.getAccessToken().getTokenValue();
	}

}
