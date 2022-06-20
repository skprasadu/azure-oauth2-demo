package com.itva.controllers;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itva.model.TitusAttribute;
import com.itva.model.TitusAttributes;
import com.itva.model.TitusDocument;
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

	@GetMapping("listTitusAuditLogs")
	public List<TitusAttributes> listTitusAuditLogs() {
		logger.debug("******* In listTitusAuditLogs");
		
		List<TitusDocument> tds = titusDocumentRepository.findAll();
		List<TitusAttribute> taList = titusAttributeRepository.findAll();

		List<TitusAttributes> list = Util.convertToTitusAttributes(tds, taList);

		Collections.sort(list, (s1, s2) -> s2.getLoggedTime().compareTo(s1.getLoggedTime()));

		return list;
	}

}
