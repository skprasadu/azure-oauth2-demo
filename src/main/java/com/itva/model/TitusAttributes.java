package com.itva.model;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TitusAttributes {
	private String documentName;
	private String userName;
	private String accessType;
	private String appVersion;
	private String containsCUI;
	private String containsECI;
	private String designation;
	private String docSecurity;
	private String hyperlinksChanged;
	private String linksUpToDate;
	private String proprietary;
	private String scaleCrop;
	private String shareDoc;
	private String titusGUID;
	private String visualMark;
	private Timestamp loggedTime;
}
