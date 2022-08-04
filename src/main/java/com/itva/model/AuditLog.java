package com.itva.model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditLog {
	private Timestamp creationTime;
	private String userId;
	private String operation;
	private String siteUrl;
	private String sourceFileName;
}
