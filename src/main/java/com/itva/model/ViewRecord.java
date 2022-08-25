package com.itva.model;

import java.sql.Timestamp;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ViewRecord {
	private String fileName;
	private Timestamp activityDateTime;
	@EqualsAndHashCode.Exclude
	private String displayName;
	private String email;
	
	@EqualsAndHashCode.Exclude
	private Map<String, Object> fields;

}
