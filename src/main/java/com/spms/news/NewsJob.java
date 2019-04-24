package com.spms.news;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.spms.Util;
import com.spms.database.SPMSDB;
import com.spms.ticker.live.TickerDAO;

public class NewsJob extends Thread {
	private static final Logger log = LogManager.getLogger(NewsJob.class);
	
	private TickerNewsDAO tnd;
	private String[] symbols;
	
	NewsJob(String ticker, TickerNewsDAO dao) { 
        this.symbols = ticker.split(",");
        this.tnd = dao;
	}
	
	public void fetch() throws SQLException, ParseException, MalformedURLException, org.json.simple.parser.ParseException {
		tnd = new TickerNewsDAO();
		for (String sym : symbols) {
			JSONArray symArticles = NewsAggregator.getArticles(sym);
			if (symArticles != null)
				for (int i = 0; i < symArticles.size(); i++) {
					tnd.insertNews((JSONObject)symArticles.get(i));
			}
		}
	}
	
	@Override
	public void run() {
		try {
			fetch();
		} catch (ParseException e) {
			log.error(Util.stackTraceToString(e));
		} catch (SQLException e) {
			log.info("Unique constraint ");
		} catch (MalformedURLException e) {
			log.error(Util.stackTraceToString(e));
		} catch (org.json.simple.parser.ParseException e) {
			log.error(Util.stackTraceToString(e));
		} catch (NullPointerException e) {
			
		}
	}
}
