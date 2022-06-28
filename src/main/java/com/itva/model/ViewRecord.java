package com.itva.model;

import java.sql.Timestamp;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ViewRecord {
	private String fileName;
	private Timestamp activityDateTime;
	private String displayName;
	
	@EqualsAndHashCode.Exclude
	private Map<String, String> fields;

}
