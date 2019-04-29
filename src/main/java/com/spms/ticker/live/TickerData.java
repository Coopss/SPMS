package com.spms.ticker.live;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.spms.database.SPMSDB;

public class TickerData {
	private static final Logger log = LogManager.getLogger(TickerData.class);
	private JSONObject data;
	public Date dateobj;
	public String ticker;
	public String date;
	public String high;
	public String low;
	public String average;
	public String volume;
	public String notional;
	public String numberOfTrades;
	public String marketHigh;
	public String marketLow;
	public String marketAverage;
	public String marketVolume;
	public String marketNotional;
	public String marketNumberOfTrades;
	public String open;
	public String close;
	public String marketOpen;
	public String marketClose;
	public String changeOverTime;
	public String marketChangeOverTime;
	
	private String get(String key) {
		Object v;
		if (data.containsKey(key)) {
			v = data.get(key);
			if (v != null) {
				return v.toString();
			}		
		} else {
			log.warn("data does not have key : " + key);
		}
		return null;
	}
	
	public TickerData() {
		
		
	}
	
	public TickerData(JSONObject data, String ticker) throws ParseException {
		StringBuilder dateString = new StringBuilder();
		dateString.append(data.get("date"));
		dateString.append(" " + data.get("label"));
		this.data = data;
		this.ticker = ticker;

		Date dateobj;
		try {
			dateobj = new SimpleDateFormat("yyyyMMdd hh:mm aa").parse(dateString.toString());  
		} catch (ParseException e) {
			dateobj = new SimpleDateFormat("yyyyMMdd hh aa").parse(dateString.toString());
		}
		this.dateobj = dateobj;
		date = SPMSDB.getMSSQLDatetime(dateobj);
		high = get("high");
		low = get("low");
		average = get("average");
		volume = get("volume");
		notional = get("notional");
		numberOfTrades = get("numberOfTrades");
		marketHigh = get("marketHigh");
		marketLow = get("marketLow");
		marketAverage = get("marketAverage");
		marketVolume = get("marketVolume");
		marketNotional = get("marketNotional");
		marketNumberOfTrades = get("marketNumberOfTrades");
		open = get("open");
		close = get("close");
		marketOpen = get("marketOpen");
		marketClose = get("marketClose");
		changeOverTime = get("changeOverTime");
		marketChangeOverTime = get("marketChangeOverTime");
	}


	
}
