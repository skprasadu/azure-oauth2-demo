package com.itva.model.activitiespayload;

import lombok.Data;

@Data
public class Activities {
	private String id;
	private String activityDateTime;

	private Location location;
	
    private Actor actor;
}
