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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itva.model.TitusAttribute;
import com.itva.model.TitusAttributes;
import com.itva.model.TitusDocument;
import com.itva.model.ViewRecord;
import com.itva.model.activitiespayload.Activities;
import com.itva.model.activitiespayload.VisitorsPayload;
import com.itva.repositories.TitusAttributeRepository;
import com.itva.repositories.TitusDocumentRepository;
import com.itva.util.Util;

@RestController
@RequestMapping("/api")
public class TitusAuditLogController {
	private Logger logger = LoggerFactory.getLogger(TitusAuditLogController.class);

	@Autowired
	private TitusDocumentRepository titusDocumentRepository;

	@Autowired
	private TitusAttributeRepository titusAttributeRepository;
	
	@Autowired
	private OAuth2AuthorizedClientService clientService;

	@GetMapping("listTitusAuditLogs")
	public List<TitusAttributes> listTitusAuditLogs(@RequestParam("checkForChanges") Boolean checkForChanges) {		
		logger.debug("******* In listTitusAuditLogs");
		
		if(checkForChanges) {
			List<TitusDocument> tds = titusDocumentRepository.findAll();
			List<TitusAttribute> taList = titusAttributeRepository.findAll();
	
			List<TitusAttributes> list = Util.convertToTitusAttributes(tds, taList);
		
			checkForNewViewsAndInsert(list);
		}
		
		List<TitusDocument> tds = titusDocumentRepository.findAll();
		List<TitusAttribute> taList = titusAttributeRepository.findAll();

		List<TitusAttributes> list = Util.convertToTitusAttributes(tds, taList);
		Collections.sort(list, (s1, s2) -> s2.getLoggedTime().compareTo(s1.getLoggedTime()));

		return list;
	}

	private void checkForNewViewsAndInsert(List<TitusAttributes> list) {
		Set<ViewRecord> newViewedSet = new HashSet<>();
		String token = getToken();
		
		for(TitusAttributes t: list) {
			VisitorsPayload visitorsPayload = Util.getItemIdDetails(t.getDocumentName(), token);
			if(visitorsPayload != null) {
				
				for(Activities activities : visitorsPayload.getActivities()) {
					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
					LocalDateTime localdatetime = LocalDateTime.parse(activities.getActivityDateTime(), format);
					Timestamp ts = Timestamp.valueOf(localdatetime);
					newViewedSet.add(new ViewRecord(t.getDocumentName(), ts, activities.getActor().getUser().getDisplayName()));
				}
			}
		}
		logger.debug("********************");
		//System.out.println("newViewedSet=" + newViewedSet);
		
		List<TitusAttributes> readList = list.stream().filter(x -> x.getAccessType().equals("READ")).collect(Collectors.toList());
		Set<ViewRecord> existingViewedSet = new HashSet<>();
		
		for(TitusAttributes t: readList) {
			existingViewedSet.add(new ViewRecord( t.getDocumentName(), t.getLoggedTime(), t.getUserName()));
		}
				
		logger.debug("********************");
		logger.debug("existingViewedSet=" + existingViewedSet);
		
		for(ViewRecord vr: newViewedSet) {
			logger.debug("VR=" + vr);
			if(!existingViewedSet.contains(vr)) {
				titusDocumentRepository.save(new TitusDocument(vr.getFileName(), vr.getDisplayName(), "READ", vr.getActivityDateTime()));
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
