package com.itva.model.activitiespayload;

import lombok.Data;

@Data
public class Address {
	
    private String city;
    private String countryOrRegion;
    private String postalCode;
    private String state;
    private String street;

}
