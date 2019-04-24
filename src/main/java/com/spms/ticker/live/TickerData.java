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
	Date dateobj;
	String ticker;
	String date;
	String high;
	String low;
	String average;
	String volume;
	String notional;
	String numberOfTrades;
	String marketHigh;
	String marketLow;
	String marketAverage;
	String marketVolume;
	String marketNotional;
	String marketNumberOfTrades;
	String open;
	String close;
	String marketOpen;
	String marketClose;
	String changeOverTime;
	String marketChangeOverTime;
	
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
