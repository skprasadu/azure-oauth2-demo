package com.itva.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TitusDocument {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String documentName;
	private String userName;
	private String accessType;
	private Timestamp loggedTime;
	
	public TitusDocument(String documentName, String userName, String accessType, Timestamp loggedTime) {
		this.documentName = documentName;
		this.userName = userName;
		this.accessType = accessType;
		this.loggedTime = loggedTime;
	}
}
