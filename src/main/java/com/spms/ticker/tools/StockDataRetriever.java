package com.spms.ticker.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.spms.config.Config;

public class StockDataRetriever {
	private static String urlLead = Config.dataEndpoint;
	
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
	
	public static JSONArray get(String urlExt) throws MalformedURLException, ParseException {
		String resp = getResponse(urlExt);
		return stringToJSONArray(resp);
	}
}
