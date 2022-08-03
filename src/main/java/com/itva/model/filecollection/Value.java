package com.itva.model.filecollection;

import java.util.Map;

import lombok.Data;

@Data
public class Value {
	private Integer id;
	private String webUrl;
	private Map<String, Object> fields;

}
