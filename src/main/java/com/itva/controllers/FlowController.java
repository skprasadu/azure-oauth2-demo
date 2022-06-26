package com.itva.controllers;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itva.model.TitusDocument2;
import com.itva.repositories.TitusDocument2Repository;

@RestController
public class FlowController {
	@Autowired
	private TitusDocument2Repository titusDocument2Repository;

	
	@PostMapping("/flow/addTitusAuditLog")
	public void addTitusAuditLog(@RequestBody TitusDocument2 titusDocument2) {
		Long datetime = System.currentTimeMillis();
		titusDocument2.setLoggedTime(new Timestamp(datetime));
		titusDocument2Repository.save(titusDocument2);
	}
}
