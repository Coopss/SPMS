package com.spms.ticker.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spms.Util;
import com.spms.api.AuthenticationService;
import com.spms.config.Config;

public class Requests {
	private static final Logger log = LogManager.getLogger(Requests.class);
	
	public static String followRedirect(String url) {
		try {
			return followRedirect(new URL(url));
		} catch (MalformedURLException e) {
			log.error(Util.stackTraceToString(e));
		}
		return null;
	}
	public static String followRedirect(URL url) {
	    try {
	    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    	conn.setReadTimeout(10000);

	    	log.info("Request URL ... " + url);

	    	boolean redirect = false;

	    	// normally, 3xx is redirect
	    	int status = conn.getResponseCode();
	    	if (status != HttpURLConnection.HTTP_OK) {
	    		if (status == HttpURLConnection.HTTP_MOVED_TEMP
	    			|| status == HttpURLConnection.HTTP_MOVED_PERM
	    				|| status == HttpURLConnection.HTTP_SEE_OTHER)
	    		redirect = true;
	    	}

	    	log.info("Response Code ... " + status);

	    	if (redirect) {

	    		// get redirect url from "location" header field
	    		String newUrl = conn.getHeaderField("Location");

	    		// get the cookie if need, for login
	    		String cookies = conn.getHeaderField("Set-Cookie");

	    		// open the new connnection again
	    		conn = (HttpURLConnection) new URL(newUrl).openConnection();
	    		conn.setRequestProperty("Cookie", cookies);
	    								
	    		log.info("Redirect to URL : " + newUrl);

	    	}

	    	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    	String inputLine;
	    	StringBuffer output = new StringBuffer();

	    	while ((inputLine = in.readLine()) != null) {
	    		output.append(inputLine);
	    		output.append("\n");
	    	}
	    	
	    	in.close();

	    	return output.toString();

        } catch (Exception e) {
        	log.error(Util.stackTraceToString(e));
        }
	    
		return null;
	}
}
