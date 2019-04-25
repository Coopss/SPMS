package com.spms.ticker.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.spms.Util;
import com.spms.api.AuthenticationService;
import com.spms.config.Config;

public class Requests {
	private static final Logger log = LogManager.getLogger(Requests.class);
	private static String urlLead = Config.dataEndpoint;
	
	public static enum ReturnType {
		object,
		array
	};
	
	public static String followRedirect(String url) throws Exception {
		try {
			return followRedirect(new URL(url));
		} catch (MalformedURLException e) {
			log.error(Util.stackTraceToString(e));
		}
		return null;
	}
	public static String followRedirect(URL url) throws Exception {
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
        	if (e.getMessage().contains("424") || e.getMessage().contains("423") || e.getMessage().contains("429")) {
        		throw e;
        	}
        	
        	log.error(Util.stackTraceToString(e));
        }
	    
		return null;
	}
	
	public static String getResponse(String urlExt) throws MalformedURLException {
		URL url = new URL(urlLead + urlExt);
		
		try {
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				try {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				in.close();
				return content.toString();
			} catch (FileNotFoundException E) {
				return "";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private static JSONArray stringToJSONArray(String jsonString) throws ParseException {
		JSONParser parser = new JSONParser();
		return (JSONArray) parser.parse(jsonString);
	}
	
	private static JSONObject stringToJSONObject(String jsonString) throws ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(jsonString);
	}
	
	public static Object get(String urlExt, ReturnType type) throws MalformedURLException, ParseException {
		String resp = getResponse(urlExt);
		if (type == ReturnType.array) {
			return stringToJSONArray(resp);
		} else {
			return stringToJSONObject(resp);
		}
	}
	
}
