package com.spms.ticker.tools;

import java.util.HashMap;

public class JSONObject {
	private HashMap<String, String> vars;
	
	public JSONObject (String json) {
		vars = new HashMap<String, String>();
		
		json = json.substring(0, json.length() - 1);
		
		String[] atbs = json.split(",");
		
		for (String a : atbs) {
			String[] vals = a.split(":");
			vars.put(vals[0], vals[1]);
		}
	}
	
	public String getValue(String key) {
		return vars.get(key);
	}
	
}
