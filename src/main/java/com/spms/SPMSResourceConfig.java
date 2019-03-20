package com.spms;

import org.glassfish.jersey.server.ResourceConfig;

import com.spms.api.filters.CORSFilter;

public class SPMSResourceConfig extends ResourceConfig{
	
	public SPMSResourceConfig() {
		register(CORSFilter.class);
	}
	
}
