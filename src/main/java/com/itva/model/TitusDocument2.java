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
public class TitusDocument2 {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String siteFullPath;
	private String documentName;
	private String userName;
	private String userId;
	private String accessType;
	private Timestamp loggedTime;
	
	private String eci;
	private String eciCoC;
	private String eciJuris;
	private String eciClass;
	private String export;
	private String exAuth;
	private String containsCUI;
	private String cui;
	private String dissemination;
	private String proprietary;
	private String proprietaryType;
	private String proprietaryStatement;
}
