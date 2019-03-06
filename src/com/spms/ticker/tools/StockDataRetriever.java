package com.spms.ticker.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockDataRetriever {
	private String url_lead = "https://api.iextrading.com/1.0";
	
	StockDataRetriever () {
		
	}
	
	public String getResponse(String ext) throws MalformedURLException {
		URL url = new URL(url_lead + ext);
		
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			in.close();
			return content.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public ArrayList<String> getJSONSFromString(String json_arr) {
		ArrayList<String> jsons = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(json_arr);
		
		while (matcher.find()) {
			jsons.add(json_arr.substring(matcher.start(), matcher.end()));
		}
		
		for (String j : jsons) {
			System.out.println(j);
		}
		
		return jsons;
	}
}
