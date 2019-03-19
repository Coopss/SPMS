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
	
	private String makeDatetime(String date, String time) {
		String newDate = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
		String newTime = time + ":00";
		
		return newDate + " " + newTime;
	}
	
	public String getValue(String key) {
		return vars.get(key);
	}
	
	public String getValues(String[] values) {
		String out = "(";
		for (String s : values) {
			String tmp = this.getValue(s);
			
			if (s.equals("date")) {
				tmp = this.makeDatetime(this.getValue("date"), this.getValue("minute"));
			}
			
			out += tmp + ",";
		}
		out = out.substring(0, out.length() - 1);
		out += ")";
		
		return out;
	}
	
}
