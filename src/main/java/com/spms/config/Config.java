package com.spms.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Config {
	
	/* SPMS General Config */
	public final static String version = "1.0.0-alpha";
	
	/* SPMS DB Config */
	public final static String dbHostName ="spms.database.windows.net";
	public final static String masterDB = "SPMS-db";
	public final static String dbuser = "spms-admin";
	public final static String dbPassword = "Password1";
	
	/* SPMS Data Config */
	public final static String dataEndpoint = "https://api.iextrading.com/1.0";
	
	/* Symbols */
	public static URL endpoint;
	static {
		try {
			endpoint = new URL("http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nasdaq&render=download");
		} catch (Exception e) {		
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
}
