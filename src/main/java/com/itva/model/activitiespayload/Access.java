package com.itva.model.activitiespayload;

import lombok.Data;

@Data
public class Access {
	private Integer actionCount;
    private Integer actorCount;
    private Integer timeSpentInSeconds;
}
