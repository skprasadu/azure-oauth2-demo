package com.itva.model.activitiespayload;

import java.util.ArrayList;

import lombok.Data;

@Data
public class VisitorsPayload {
	private String aggregationInterval;
    private String startDateTime;
    private String endDateTime;
    private boolean isTrending;
    
    private Access access;
    private ArrayList<Activities> activities;
}
